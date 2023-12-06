            /* Copyright (c) 2017 FIRST. All rights reserved.
             *
             * Redistribution and use in source and binary forms, with or without modification,
             * are permitted (subject to the limitations in the disclaimer below) provided that
             * the following conditions are met:
             *
             * Redistributions of source code must retain the above copyright notice, this list
             * of conditions and the following disclaimer.
             *
             * Redistributions in binary form must reproduce the above copyright notice, this
             * list of conditions and the following disclaimer in the documentation and/or
             * other materials provided with the distribution.
             *
             * Neither the name of FIRST nor the names of its contributors may be used to endorse or
             * promote products derived from this software without specific prior written permission.
             *
             * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
             * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
             * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
             * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
             * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
             * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
             * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
             * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
             * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
             * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
             * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
             */
            package org.firstinspires.ftc.teamcode;

    import com.qualcomm.robotcore.eventloop.opmode.Disabled;
    import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
    import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
    import com.qualcomm.robotcore.hardware.CRServo;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;
    import com.qualcomm.robotcore.hardware.Servo;
    import com.qualcomm.robotcore.util.ElapsedTime;
    import com.qualcomm.robotcore.util.Range;


    /*
     * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
     * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
     * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
     * class is instantiated on the Robot Controller and executed.
     *
     * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
     * It includes all the skeletal structure that all linear OpModes contain.
     *
     * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
     * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
     */

    @TeleOp
    public class BasicOP extends LinearOpMode {

        // Declare OpMode members.
        private ElapsedTime runtime = new ElapsedTime();
        private DcMotor FrontDrive = null;
        private DcMotor BackDrive = null;
        private DcMotor leftDrive = null;
        private DcMotor rightDrive = null;
        private Servo rightServo = null;
        private Servo leftServo = null;
        private final double Kp_servo1 = 0.1;
        private final double Ki_servo1 = 0.01;
        private final double Kd_servo1 = 0.05;

        // PID constants for the second servo
        private final double Kp_servo2 = 0.1;
        private final double Ki_servo2 = 0.01;
        private final double Kd_servo2 = 0.05;

        // Desired positions for the servos
        private double desiredServoPosition1 = 0.5; // Adjust as needed
        private double desiredServoPosition2 = 0.5; // Adjust as needed

        // Variables for PID control of servo 1
        private double integral_servo1 = 0;
        private double previousError_servo1 = 0;

        // Variables for PID control of servo 2
        private double integral_servo2 = 0;
        private double previousError_servo2 = 0;

        private double currentServoPosition1;
        private double currentServoPosition2;

        private double desiredServoPosition = 0.5; // Adjust this as needed

        private double integral = 0;
        private double previousError = 0;
        private double slow = 1;

        @Override
        public void runOpMode() {
            telemetry.addData("Status", "Initialized");
            telemetry.update();

            // Initialize the hardware variables. Note that the strings used here as parameters
            // to 'get' must correspond to the names assigned during the robot configuration
            // step (using the FTC Robot Controller app on the phone).
            FrontDrive = hardwareMap.get(DcMotor.class, "Motor01"); //0
            BackDrive = hardwareMap.get(DcMotor.class, "Motor02"); //1
            leftDrive  = hardwareMap.get(DcMotor.class, "Motor03"); //2
            rightDrive = hardwareMap.get(DcMotor.class, "Motor04"); //3
            leftServo = hardwareMap.get(Servo.class, "servo_1"); //0
            rightServo = hardwareMap.get(Servo.class, "servo_2"); //1
            // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
            // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
            // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
            FrontDrive.setDirection(DcMotor.Direction.FORWARD);
            BackDrive.setDirection(DcMotor.Direction.REVERSE);
            leftDrive.setDirection(DcMotor.Direction.REVERSE);
            rightDrive.setDirection(DcMotor.Direction.FORWARD);

            // Wait for the game to start (driver presses PLAY)
            waitForStart();
            runtime.reset();

            // run until the end of the match (driver presses STOP)
            while (opModeIsActive()) {

                double currentServoPosition1 = leftServo.getPosition();
                double currentServoPosition2 = rightServo.getPosition();

                // Calculate the errors for both servos


                // Setup a variable for each drive wheel to save power level for telemetry
                double leftPower = 0;
                double rightPower = 0;
                double frontPower = 0;
                double backPower = 0;


                if (gamepad1.left_bumper){
                    desiredServoPosition1 = 1; // Adjust these values accordingly
                    desiredServoPosition2 = 1;
                    PidCalulate();
                    slow = 1;
                }

                if (gamepad1.right_bumper){
                    desiredServoPosition1 = -1; // Adjust these values accordingly
                    desiredServoPosition2 = -1;
                    PidCalulate();
                    slow = 3;
                }


                // Choose to drive using either Tank Mode, or POV Mode
                // Comment out the method that's not used.  The default below is POV.

                // POV Mode uses left stick to go forward, and right stick to turn.
                // - This uses basic math to combine motions and is easier to drive straight.
                double drive = -gamepad1.right_stick_y;
                double turn  =  gamepad1.right_stick_x;
    //          leftPower    = Range.clip((drive + turn) * 100, -5.0, 100.0) ;
    //          rightPower   = Range.clip((drive - turn) * 100, -5.0, 100.0) ;

                // Tank Mode uses one stick to control each wheel.
                // - This requires no math, but it is hard to drive forward slowly and keep straight.

                double x = gamepad1.left_stick_x;
                double y = gamepad1.left_stick_y;

                double rotate = gamepad1.right_stick_x;

                if (y == 1){
                    backPower = y;
                    rightPower = y;
                    frontPower = y;
                    leftPower = y;
                } else if (y == -1) {
                    backPower = y;
                    rightPower = y;
                    frontPower = y;
                    leftPower = y;
                }

                if (x == 1){
                    backPower = x;
                    rightPower = -x;
                    frontPower = x;
                    leftPower = -x;
                } else if (x == -1) {
                    backPower = x; //1
                    frontPower = x; //0
                    rightPower = -x; //3
                    leftPower = -x; //2
                }

                if (rotate == 1){
                    rightPower = -rotate;
                    leftPower =   rotate;
                    frontPower = -rotate;
                    backPower =   rotate;
                } else if (rotate == -1){
                    rightPower =-rotate;
                    leftPower =  rotate;
                    frontPower =-rotate;
                    backPower =  rotate;
                }



    //             leftPower  =  -gamepad1.left_stick_y ;
    //             rightPower =  gamepad1.left_stick_y ;
    //             frontPower =  gamepad1.left_stick_x;
    //             backPower =  -gamepad1.left_stick_x;
                // Send calculated power to wheels
                leftDrive.setPower(leftPower / slow);
                rightDrive.setPower(rightPower / slow);
                FrontDrive.setPower(frontPower / slow);
                BackDrive.setPower(backPower / slow);

                // Show the elapsed game time and wheel power.
                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.addData("Status", "Run Time: " + gamepad1.left_trigger);
                telemetry.addData("Speed", slow);
                telemetry.addData("Direction", "x (%.2f), y (%.2f)", x, y);
                telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower / slow, rightPower / slow);
                telemetry.addData("Motors", "front (%.2f), back (%.2f)", frontPower / slow, backPower / slow);
                telemetry.addData("Servo_leftPostion", leftServo.getPosition());
                telemetry.addData("Servo_rightPostion", rightServo.getPosition());
                telemetry.update();
            }
        }
        private void PidCalulate(){
            double error_servo1 = desiredServoPosition1 - currentServoPosition1;
            double error_servo2 = desiredServoPosition2 - currentServoPosition2;

            // PID calculations for servo 1
            double P_servo1 = Kp_servo1 * error_servo1;
            integral_servo1 += error_servo1;
            double I_servo1 = Ki_servo1 * integral_servo1;
            double derivative_servo1 = error_servo1 - previousError_servo1;
            double D_servo1 = Kd_servo1 * derivative_servo1;
            double output_servo1 = P_servo1 + I_servo1 + D_servo1;
            double newServoPosition1 = Range.clip(currentServoPosition1 + output_servo1, 0, 1);
            leftServo.setPosition(newServoPosition1);
            previousError_servo1 = error_servo1;

            double P_servo2 = Kp_servo2 * error_servo2;
            integral_servo2 += error_servo2;
            double I_servo2 = Ki_servo2 * integral_servo2;
            double derivative_servo2 = error_servo2 - previousError_servo2;
            double D_servo2 = Kd_servo2 * derivative_servo2;
            double output_servo2 = P_servo2 + I_servo2 + D_servo2;
            double newServoPosition2 = Range.clip(currentServoPosition2 + output_servo2, 0, 1);
            rightServo.setPosition(newServoPosition2);
            previousError_servo2 = error_servo2;

        }
    }

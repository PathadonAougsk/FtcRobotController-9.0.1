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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;


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

@TeleOp(name="control", group="Linear OpMode")
// wheel speed
public class control extends LinearOpMode {

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Player One control Movement + Paper plane
        // Player Two control Servo + hand
        ElapsedTime runtime = new ElapsedTime();

        DcMotorEx Motor01 = hardwareMap.get(DcMotorEx.class, "Motor01");
        DcMotorEx Motor02 = hardwareMap.get(DcMotorEx.class, "Motor02");
        DcMotorEx Motor03 = hardwareMap.get(DcMotorEx.class, "Motor03");
        DcMotorEx Motor04 = hardwareMap.get(DcMotorEx.class, "Motor04");
        DcMotorEx _GearMotor = hardwareMap.get(DcMotorEx.class, "Motor05");

        Motor01.setDirection(DcMotor.Direction.FORWARD);
        Motor02.setDirection(DcMotor.Direction.REVERSE);
        Motor03.setDirection(DcMotor.Direction.REVERSE);
        Motor04.setDirection(DcMotor.Direction.FORWARD);

        Motor01.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor02.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor03.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor04.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Servo servo_left = hardwareMap.get(Servo.class, "Servo_left");
        Servo servo_right = hardwareMap.get(Servo.class, "Servo_right");
        Servo arm_right = hardwareMap.get(Servo.class, "Servo_arm1");
        Servo arm_left = hardwareMap.get(Servo.class, "Servo_arm2");

        servo_right.setDirection(Servo.Direction.REVERSE);
        double velocity_still = 0;
//      Go calculate that shit.
        double velocity_forward = 1400;
        double velocity_backward = -1400;
        double brake = 0;
        double range_motor = 0;

        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;

        double rotate = gamepad1.right_stick_x;

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.left_bumper){
                range_motor = range_motor + 0.005;
                servo_left.getPosition();
                servo_right.getPosition();
                servo_left.setPosition(range_motor);
                servo_right.setPosition(range_motor);
            }

            if (gamepad1.right_bumper){
                range_motor = range_motor - 0.005;
                servo_left.setPosition(range_motor);
                servo_right.setPosition(range_motor);
            }

            if (gamepad1.x){
                // Let's check for the encoder for the height so we can set the limit
                // if (encoder < height){
                _GearMotor.setVelocity(velocity_forward);
            }

            if (gamepad1.a){
                // if (encoder > miniHeight)
                _GearMotor.setVelocity(velocity_backward);
            }

            if(gamepad1.dpad_up){
                Motor01.setVelocity(velocity_forward);
                Motor02.setVelocity(velocity_forward);
                Motor03.setVelocity(velocity_forward);
                Motor04.setVelocity(velocity_forward);
            }

            if(gamepad1.dpad_down){
                Motor01.setVelocity(velocity_backward);
                Motor02.setVelocity(velocity_backward);
                Motor03.setVelocity(velocity_backward);
                Motor04.setVelocity(velocity_backward);
            }

            if(gamepad1.dpad_left){
                Motor01.setVelocity(velocity_backward);
                Motor02.setVelocity(velocity_backward);
                Motor03.setVelocity(velocity_forward);
                Motor04.setVelocity(velocity_forward);
            }

            if(gamepad1.dpad_right){
                Motor01.setVelocity(velocity_forward);
                Motor02.setVelocity(velocity_forward);
                Motor03.setVelocity(velocity_backward);
                Motor04.setVelocity(velocity_backward);
            }

            telemetry.addData("time running", "Run Time: " + runtime.toString());
            telemetry.addData("Currently at",  " at %7d :%7d",
                    Motor01.getCurrentPosition(), Motor02.getCurrentPosition());
            telemetry.addData("Currently at",  " at %7f :%7f",servo_left.getPosition(), servo_right.getPosition());
            telemetry.update();
        }
    }
}

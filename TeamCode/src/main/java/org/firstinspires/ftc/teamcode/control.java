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

// copyright to do whatever you fucking want.
@TeleOp(name="control", group="Linear OpMode")
public class control extends LinearOpMode {
    double range_motor = 0;
    DcMotorEx Motor01, Motor02, Motor03, Motor04, _GearMotor;
    Servo servo_left, servo_right, arm_right, arm_left;

    boolean isRightBumperPressed, isLeftBumperPressed;
    double isRightTrigger,isLeftTrigger;
    double velocity_still, Xcontroller, Ycontroller = 0;
    double servoPos;
    double velocity = 1400;

    @Override
    //gonna need to fix to servo like real fast.
    public void runOpMode() {
        initializeHardware();
        waitForStart();

        while (opModeIsActive()) {
            handleGamepadInput();
            telemetryUpdates();
            servoPos = servo_left.getPosition();

            if (gamepad2.left_bumper){
                range_motor += 0.005f;
                servo_left.setPosition(range_motor);
                servo_right.setPosition(range_motor);
            }

            if (gamepad2.right_bumper){
                range_motor -= 0.005f;
                servo_right.setPosition(range_motor);
            }
        }
    }

    private void initializeHardware() {
        Motor01 = hardwareMap.get(DcMotorEx.class, "Motor01");
        Motor02 = hardwareMap.get(DcMotorEx.class, "Motor02");
        Motor03 = hardwareMap.get(DcMotorEx.class, "Motor03");
        Motor04 = hardwareMap.get(DcMotorEx.class, "Motor04");

//        _GearMotor = hardwareMap.get(DcMotorEx.class, "Motor05");

        servo_left = hardwareMap.get(Servo.class, "Servo_left");
        servo_right = hardwareMap.get(Servo.class, "Servo_right");
        arm_right = hardwareMap.get(Servo.class, "Servo_arm1");
        arm_left = hardwareMap.get(Servo.class, "Servo_arm2");

        Motor01.setDirection(DcMotor.Direction.FORWARD);
        Motor02.setDirection(DcMotor.Direction.REVERSE);
        Motor03.setDirection(DcMotor.Direction.REVERSE);
        Motor04.setDirection(DcMotor.Direction.FORWARD);

        Motor01.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor02.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor03.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor04.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        servo_right.setDirection(Servo.Direction.REVERSE);
    }

    private void handleGamepadInput() {
        Xcontroller = gamepad1.left_stick_x;
        Ycontroller = gamepad1.left_stick_y;

        // Rest of your gamepad input handling...
        isLeftBumperPressed = gamepad2.left_bumper;
        isRightBumperPressed = gamepad2.right_bumper;
        isRightTrigger = gamepad2.right_trigger;
        isLeftTrigger = gamepad2.left_trigger;


        // this hard coding is killing me
        if (Ycontroller == 1){
            Motor01.setVelocity(velocity);
            Motor02.setVelocity(velocity);
            Motor03.setVelocity(velocity);
            Motor04.setVelocity(velocity);
        } else if (Ycontroller == -1){
            Motor01.setVelocity(-velocity);
            Motor02.setVelocity(-velocity);
            Motor03.setVelocity(-velocity);
            Motor04.setVelocity(-velocity);
        } else if (Xcontroller == 1){
            Motor01.setVelocity(velocity);
            Motor02.setVelocity(velocity);
            Motor03.setVelocity(-velocity);
            Motor04.setVelocity(-velocity);
        } else if (Xcontroller == -1){
            Motor01.setVelocity(-velocity);
            Motor02.setVelocity(-velocity);
            Motor03.setVelocity(velocity);
            Motor04.setVelocity(velocity);
        } else {
            Motor01.setVelocity(velocity_still);
            Motor02.setVelocity(velocity_still);
            Motor03.setVelocity(velocity_still);
            Motor04.setVelocity(velocity_still);
        }




        if (gamepad2.right_bumper) {
            servoTurn(servo_left, -0.05f);
            servoTurn(servo_left, -0.05f);
        }

        if (isLeftTrigger > 0) {
            servoTurn(arm_left, 0.05f);
        }

        if (isRightTrigger > 0) {
            servoTurn(arm_left, -0.05f);
        }

        isLeftBumperPressed = false;
        isRightBumperPressed = false;
    }

    private void servoTurn(Servo servo, float desiredSpeed) {
        range_motor += desiredSpeed;
        servo.setPosition(range_motor);
    }

    private void telemetryUpdates() {

        telemetry.addData("Right", isRightTrigger);
        telemetry.addData("Left", isLeftTrigger);
        telemetry.addData("ServoPos", servoPos);
        telemetry.update();
    }
}

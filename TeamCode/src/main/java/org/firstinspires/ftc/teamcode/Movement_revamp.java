package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.sql.Time;
import java.util.Timer;

@TeleOp(name="Movement_Revamp", group="Linear OpMode")
public class Movement_revamp extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode(){
        DcMotorEx Motor01 = hardwareMap.get(DcMotorEx.class, "Motor01");
        DcMotorEx Motor02 = hardwareMap.get(DcMotorEx.class, "Motor02");
        DcMotorEx Motor03 = hardwareMap.get(DcMotorEx.class, "Motor03");
        DcMotorEx Motor04 = hardwareMap.get(DcMotorEx.class, "Motor04");

        Motor01.setDirection(DcMotor.Direction.FORWARD);
        Motor02.setDirection(DcMotor.Direction.REVERSE);
        Motor03.setDirection(DcMotor.Direction.REVERSE);
        Motor04.setDirection(DcMotor.Direction.FORWARD);

        Motor01.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor02.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor03.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor04.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        DcMotorEx GearMotor = hardwareMap.get(DcMotorEx.class, "GearMotor");
        GearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Servo Servo_arm1 = hardwareMap.get(Servo.class, "servo_arm1");
        Servo Servo_arm2 = hardwareMap.get(Servo.class, "servo_arm2");
        Servo Servo_hand1 = hardwareMap.get(Servo.class, "servo_hand1");
        Servo Servo_hand2 = hardwareMap.get(Servo.class, "servo_hand2");
        Servo Pixel_servo = hardwareMap.get(Servo.class, "pixel_servo");
        Servo Airplane = hardwareMap.get(Servo.class, "air_servo");

        Servo_arm1.setDirection(Servo.Direction.REVERSE);
        Servo_hand1.setDirection(Servo.Direction.REVERSE);
        final double HAND_ADJUSTMENT = 0.05; // Amount to adjust the servo position
        final long SERVO_UPDATE_INTERVAL = 200; // Time interval in milliseconds for servo updates\
        long lastServoUpdateTime = 0;
        double mappedServoPosition;
        double velocity = 1400;
        double still = 0;

        double x,y,rotate;
        boolean trigger_left;
        boolean trigger_right;
        double range_motor_arm = 0;
        double range_motor_hand = 0;
        boolean Isit_1 = true;
        boolean isMovingToPosition1 = false;

        Pixel_servo.scaleRange(0,0.7);

//        if (Servo_hand1.getPosition() != 0 || Servo_hand2.getPosition() != 0){
//            Servo_hand1.setPosition(0);
//            Servo_hand2.setPosition(0);
//        }

        Pixel_servo.setPosition(1);
        waitForStart();


        while (opModeIsActive()){
            // after game start
            x = gamepad1.left_stick_x;
            y = gamepad1.left_stick_y;
            rotate = gamepad1.right_stick_x;

            if (range_motor_arm  > 1){
                range_motor_arm = 1;
            } else if (range_motor_arm < 0){
                range_motor_arm = 0;
            }

            if (gamepad1.right_bumper){
                velocity = 466.7;
            }

            if (gamepad1.left_bumper){
                velocity = 1400;
            }

            if (x != 1 && y != 1){
                Motor01.setVelocity(still);
                Motor02.setVelocity(still);
                Motor03.setVelocity(still);
                Motor04.setVelocity(still);
            } else if (x != -1 && y != -1){
                Motor01.setVelocity(still);
                Motor02.setVelocity(still);
                Motor03.setVelocity(still);
                Motor04.setVelocity(still);
            }

            if (y == 1){
                Motor01.setVelocity(-velocity);
                Motor02.setVelocity(-velocity);
                Motor03.setVelocity(velocity);
                Motor04.setVelocity(velocity);
            } else if (y == -1) {
                Motor01.setVelocity(velocity);
                Motor02.setVelocity(velocity);
                Motor03.setVelocity(-velocity);
                Motor04.setVelocity(-velocity);
            }

            if (x == 1){
                Motor01.setVelocity(-velocity);
                Motor02.setVelocity(-velocity);
                Motor03.setVelocity(-velocity);
                Motor04.setVelocity(-velocity);
            } else if (x == -1) {
                Motor01.setVelocity(velocity);
                Motor02.setVelocity(velocity);
                Motor03.setVelocity(velocity);
                Motor04.setVelocity(velocity);
            }

            if (rotate == 1){
                Motor01.setVelocity(-velocity);
                Motor02.setVelocity(velocity);
                Motor03.setVelocity(velocity);
                Motor04.setVelocity(-velocity);
            } else if (rotate == -1){
                Motor01.setVelocity(velocity);
                Motor02.setVelocity(-velocity);
                Motor03.setVelocity(-velocity);
                Motor04.setVelocity(velocity);
            }

            if(gamepad1.right_bumper){
                GearMotor.setVelocity(velocity);
            } else if (gamepad1.left_bumper){
                GearMotor.setVelocity(-velocity);
            }else {
                GearMotor.setVelocity(still);
            }

            if(gamepad2.right_bumper){
                range_motor_arm -= 0.5;
                Servo_arm1.setPosition(range_motor_arm);
                Servo_arm2.setPosition(range_motor_arm);
            }

            if(gamepad2.left_bumper){
                range_motor_arm += 0.05;
                Servo_arm1.setPosition(range_motor_arm);
                Servo_arm2.setPosition(range_motor_arm);
            }

            if (gamepad2.dpad_up && System.currentTimeMillis() - lastServoUpdateTime > SERVO_UPDATE_INTERVAL) {
                range_motor_hand -= HAND_ADJUSTMENT;
                Servo_hand1.setPosition(range_motor_hand);
                Servo_hand2.setPosition(range_motor_hand);
                lastServoUpdateTime = System.currentTimeMillis();
            }

            if (gamepad2.dpad_down && System.currentTimeMillis() - lastServoUpdateTime > SERVO_UPDATE_INTERVAL) {
                range_motor_hand += HAND_ADJUSTMENT;
                Servo_hand1.setPosition(range_motor_hand);
                Servo_hand2.setPosition(range_motor_hand);
                lastServoUpdateTime = System.currentTimeMillis();
            }


            if (gamepad1.a){
                Airplane.setPosition(1);
            }

            if (gamepad2.a){
                Pixel_servo.setPosition(0);
            }

            if (gamepad2.b) {
                if (Isit_1) {
                    if (!isMovingToPosition1) {
                        Pixel_servo.setPosition(0.5);
                        runtime.reset(); // Reset the timer when setting position to 0.5
                        isMovingToPosition1 = true;
                    } else {
                        if (runtime.milliseconds() >= 1500) { // Check elapsed time
                            Pixel_servo.setPosition(1);
                            Isit_1 = false;
                            isMovingToPosition1 = false;
                        }
                    }
                } else {
                    if (!isMovingToPosition1) {
                        Pixel_servo.setPosition(1);
                        runtime.reset(); // Reset the timer when setting position to 1
                        isMovingToPosition1 = true;
                    } else {
                        if (runtime.milliseconds() >= 1500) { // Check elapsed time
                            Pixel_servo.setPosition(0.5);
                            Isit_1 = true;
                            isMovingToPosition1 = false;
                        }
                    }
                }
            }
            telemetry.addData("Yo!", runtime.toString());
            telemetry.addData("Separator", "--------------------------------------------");
            telemetry.addData("Servo_arm1_Position", Servo_arm1.getPosition());
            telemetry.addData("Servo_arm2_Position", Servo_arm2.getPosition());
            telemetry.addData("Servo_hand1_Position", Servo_hand1.getPosition());
            telemetry.addData("Servo_hand2_Position", Servo_hand2.getPosition());
            telemetry.addData("Pixel_Servo", Pixel_servo.getPosition());
            telemetry.addData("Separator", "----------------Encoder_Debug------------------");
            telemetry.addData("Encoder Positions", "Motor1: " + Motor01.getCurrentPosition() +
                    ", Motor2: " + Motor02.getCurrentPosition() +
                    ", Motor3: " + Motor03.getCurrentPosition() +
                    ", Motor4: " + Motor04.getCurrentPosition());
            telemetry.addData("Is it Pressed?", gamepad2.dpad_up);
            telemetry.addData("Is it Pressed?", gamepad2.dpad_down);
            telemetry.update();
            sleep(10);
        }
    }
  }


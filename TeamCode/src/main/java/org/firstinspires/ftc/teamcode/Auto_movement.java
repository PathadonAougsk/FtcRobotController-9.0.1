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
import java.util.concurrent.Delayed;

@TeleOp(name="Auto_movement", group="Linear OpMode")
public class Auto_movement extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
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
        final long SERVO_UPDATE_INTERVAL = 100; // Time interval in milliseconds for servo updates\
        long lastServoUpdateTime = 0;
        double mappedServoPosition;
        double velocity = 1400;
        double still = 0;

        double x, y, rotate;
        boolean trigger_left;
        boolean trigger_right;
        double range_motor_arm = 0;
        double range_motor_hand = 0;
        boolean Isit_1 = true;
        boolean isMovingToPosition1 = false;

        Pixel_servo.scaleRange(0, 0.7);
        Servo_arm1.scaleRange(0, 1);
        Servo_arm2.scaleRange(0, 1);
        double KP = 0.1; // Placeholder values, adjust these for your system
        double KI = 0.01;
        double KD = 0.05;

//        private PIDServoController servoController;
        Pixel_servo.setPosition(1);
        waitForStart();


        while (opModeIsActive()) {
            if (gamepad2.right_bumper) {
                range_motor_arm -= 0.25;
                Servo_arm1.setPosition(range_motor_arm);
                Servo_arm2.setPosition(range_motor_arm);
                sleep(1000);
            }

            if (gamepad2.left_bumper) {
                range_motor_arm += 0.25;
                Servo_arm1.setPosition(range_motor_arm);
                Servo_arm2.setPosition(range_motor_arm);
                sleep(1000);
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
        }
    }
}


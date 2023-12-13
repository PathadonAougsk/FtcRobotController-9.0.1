package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Movement_Revamp", group="Linear OpMode")
public class Movement_revamp extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotor Motor01, Motor02, Motor03, Motor04, GearMotor;
    private Servo Servo_arm1, Servo_arm2, Servo_hand1, Servo_hand2, Pixel_servo, Airplane;
    private double range_motor_hand = 0;
    float slow;
    private long lastServoUpdateTime = 0;

    @Override
    public void runOpMode() {

        initializeHardware();

        waitForStart();
        while (opModeIsActive()) {
            float backPower = 0;
            float frontPower = 0;
            float rightPower = 0;
            float leftPower = 0;

            float x = gamepad1.left_stick_x;
            float y = gamepad1.left_stick_y;
            float rotate = gamepad1.right_stick_x;

            if (y == 1) {
                backPower = -y; //1
                frontPower = -y; //0
                rightPower = y; //3
                leftPower = y; //2
            }

            if (y == -1) {
                backPower = -y;
                rightPower = y;
                frontPower = -y;
                leftPower = y;
            }

            if (x == 1) {
                backPower = -x;
                rightPower = -x;
                frontPower = -x;
                leftPower = -x;
            }

            if (x == -1) {
                backPower = -x;
                rightPower = -x;
                frontPower = -x;
                leftPower = -x;
            }

            if (rotate == 1) {
                rightPower = -rotate;
                leftPower = rotate;
                frontPower = -rotate;
                backPower = rotate;
            } else if (rotate == -1) {
                rightPower = -rotate;
                leftPower = rotate;
                frontPower = -rotate;
                backPower = rotate;
            }

            Motor01.setPower((frontPower) / slow);
            Motor02.setPower((backPower) / slow);
            Motor03.setPower((leftPower) / slow);
            Motor04.setPower((rightPower) / slow);

            if (gamepad1.b || gamepad2.b) {
                Pixel_servo.setPosition(0);
                PickPixel();
            }

            if (gamepad1.a || gamepad2.a) {
                Prepare_drop();
            }

            if (gamepad1.x || gamepad2.x) {
                Pixel_control();
            }

            if (gamepad1.y || gamepad2.y) {
               dropoff();
            }

            if (gamepad1.dpad_right || gamepad1.dpad_left){
                Airplane.setPosition(1);
            }

            if (gamepad1.left_bumper || gamepad2.left_bumper) {
                GearMotor.setPower(0.45);
            } else if ((gamepad1.left_trigger) > 0 || (gamepad2.left_trigger) > 0) {
                GearMotor.setPower(-0.6);
            } else {
                GearMotor.setPower(0);
            }

            if (gamepad1.right_bumper){
                slow = 2;
            }

            Control_hand();
            telemetry_output();
        }
    }

    private void initializeHardware() {

        Motor01 = hardwareMap.get(DcMotorEx.class, "Motor01");
        Motor02 = hardwareMap.get(DcMotorEx.class, "Motor02");
        Motor03 = hardwareMap.get(DcMotorEx.class, "Motor03");
        Motor04 = hardwareMap.get(DcMotorEx.class, "Motor04");

        Motor01.setDirection(DcMotor.Direction.FORWARD);
        Motor02.setDirection(DcMotor.Direction.REVERSE);
        Motor03.setDirection(DcMotor.Direction.REVERSE);
        Motor04.setDirection(DcMotor.Direction.FORWARD);

        Motor01.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motor02.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motor03.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motor04.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        GearMotor = hardwareMap.get(DcMotor.class, "GearMotor");

        Servo_arm1 = hardwareMap.get(Servo.class, "servo_arm1");
        Servo_arm2 = hardwareMap.get(Servo.class, "servo_arm2");
        Servo_hand1 = hardwareMap.get(Servo.class, "servo_hand1");
        Servo_hand2 = hardwareMap.get(Servo.class, "servo_hand2");
        Pixel_servo = hardwareMap.get(Servo.class, "pixel_servo");
        Airplane = hardwareMap.get(Servo.class, "air_servo");

        Servo_arm1.setDirection(Servo.Direction.REVERSE);
        Servo_hand1.setDirection(Servo.Direction.REVERSE);

        Servo_arm1.scaleRange(0,1);
        Servo_arm2.scaleRange(0,1);
        Pixel_servo.scaleRange(0, 0.9);
    }

    private void Control_hand() {
        double HAND_ADJUSTMENT = 0.005;
        long SERVO_UPDATE_INTERVAL = 10;
        if (gamepad1.dpad_up && System.currentTimeMillis() - lastServoUpdateTime > SERVO_UPDATE_INTERVAL) {
            range_motor_hand -= HAND_ADJUSTMENT;
            Servo_hand1.setPosition(range_motor_hand);
            Servo_hand2.setPosition(range_motor_hand);
            lastServoUpdateTime = System.currentTimeMillis();
        }

        if (gamepad1.dpad_down && System.currentTimeMillis() - lastServoUpdateTime > SERVO_UPDATE_INTERVAL) {
            range_motor_hand += HAND_ADJUSTMENT;
            Servo_hand1.setPosition(range_motor_hand);
            Servo_hand2.setPosition(range_motor_hand);
            lastServoUpdateTime = System.currentTimeMillis();
        }
    }

    private void dropoff(){
        Servo_arm1.setPosition(0.7);
        Servo_arm2.setPosition(0.7);
        Servo_hand1.setPosition(0);
        Servo_hand2.setPosition(0);
    }

    private void PickPixel() {
        do {
            Servo_arm1.setPosition(Servo_arm1.getPosition() + 0.001);
            Servo_arm2.setPosition(Servo_arm2.getPosition() + 0.001);

        } while ((Servo_arm1.getPosition() != 1) || (Servo_arm2.getPosition() != 1));

        Servo_hand1.setPosition(0.45);
        Servo_hand2.setPosition(0.45);
        sleep(500);
        Pixel_servo.setPosition(1);
    }

    private void Prepare_drop() {
        float num = 0.001f;
        if (Servo_hand1.getPosition() > 0.45) {
            num = -num;
        }
        do {
            Servo_hand1.setPosition(Servo_hand1.getPosition() + num);
            Servo_hand1.setPosition(Servo_hand2.getPosition() + num);
        } while ((Servo_hand1.getPosition() != 0.45) || (Servo_hand2.getPosition() != 0.45));

        do {
            Servo_arm1.setPosition(Servo_arm1.getPosition() + 0.001);
            Servo_arm2.setPosition(Servo_arm2.getPosition() + 0.001);

        } while ((Servo_arm1.getPosition() != 1) || (Servo_arm2.getPosition() != 1));
        Servo_arm1.setPosition(0.8);
        Servo_arm2.setPosition(0.8);
        Servo_hand1.setPosition(0.45);
        Servo_hand2.setPosition(0.45);
    }

    private void telemetry_output() {
        telemetry.addData("Yo!", runtime.toString());
        telemetry.addData("Separator", "--------------------------------------------");
        telemetry.addData("Servo_arm1_Position", Servo_arm1.getPosition());
        telemetry.addData("Servo_arm2_Position", Servo_arm2.getPosition());
        telemetry.addData("Servo_hand1_Position", Servo_hand1.getPosition());
        telemetry.addData("Servo_hand2_Position", Servo_hand2.getPosition());
        telemetry.addData("Pixel_Servo", Pixel_servo.getPosition());
        telemetry.addData("Separator", "----------------Button_Pressed------------------");
        telemetry.addData("Speed?", slow);
        telemetry.update();
    }

    void Pixel_control(){
        if (Pixel_servo.getPosition() == 1) {
            Pixel_servo.setPosition(0.4);
        } else {
            Pixel_servo.setPosition(0);
        }
        sleep(1000);
    }
}



package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="control", group="Linear OpMode")
public class Movement_revamp extends LinearOpMode {
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

        double mappedServoPosition;
        double velocity = 1400;
        double still = 0;
        double range_motor_arm = 0;
        double range_motor_hand = 0;

        double x,y,rotate;
        boolean trigger_left;
        boolean trigger_right;

        Servo_arm1.scaleRange(160, 360);
        Servo_arm2.scaleRange(160, 360);


        waitForStart();

        while (opModeIsActive()){
            // before game start
            if (Servo_arm1.getPosition() != 0 || Servo_arm1.getPosition() != 0){
                Servo_arm1.setPosition(0);
                Servo_arm2.setPosition(0);
            } else if (Servo_hand1.getPosition() != 0 || Servo_hand2.getPosition() != 0){
                Servo_hand1.setPosition(0);
                Servo_hand2.setPosition(0);
            }

            // after game start
            x = gamepad1.left_stick_x;
            y = gamepad1.left_stick_y;
            rotate = gamepad1.right_stick_x;

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
                Motor01.setVelocity(velocity);
                Motor02.setVelocity(velocity);
                Motor03.setVelocity(velocity);
                Motor04.setVelocity(velocity);
            } else if (y == -1) {
                Motor01.setVelocity(-velocity);
                Motor02.setVelocity(-velocity);
                Motor03.setVelocity(-velocity);
                Motor04.setVelocity(-velocity);
            }

            if (x == 1){
                Motor01.setVelocity(-velocity);
                Motor02.setVelocity(-velocity);
                Motor03.setVelocity(velocity);
                Motor04.setVelocity(velocity);
            } else if (x == -1) {
                Motor01.setVelocity(velocity);
                Motor02.setVelocity(velocity);
                Motor03.setVelocity(-velocity);
                Motor04.setVelocity(-velocity);
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

            if(gamepad1.a){
                GearMotor.setVelocity(velocity);
            } else {
                GearMotor.setVelocity(still);
            }

            if(gamepad2.dpad_up){
                range_motor_arm = range_motor_arm + 0.05;
                Servo_arm1.getPosition();
                Servo_arm2.getPosition();
                Servo_arm1.setPosition(range_motor_arm);
                Servo_arm2.setPosition(range_motor_arm);
            }

            if(gamepad2.dpad_down){
                range_motor_arm = range_motor_arm - 0.05;
               Servo_arm1.getPosition();
               Servo_arm2.getPosition();
               Servo_arm1.setPosition(range_motor_arm);
               Servo_arm2.setPosition(range_motor_arm);
            }

            if(gamepad2.right_bumper){
                range_motor_hand = range_motor_hand + 0.05;
                Servo_hand1.getPosition();
                Servo_hand2.getPosition();
                Servo_hand1.setPosition(range_motor_hand);
                Servo_hand2.setPosition(range_motor_hand);
            }

            if(gamepad2.left_bumper){
                range_motor_hand = range_motor_hand - 0.05;
                Servo_hand1.getPosition();
                Servo_hand2.getPosition();
                Servo_hand1.setPosition(range_motor_hand);
                Servo_hand2.setPosition(range_motor_hand);
            }

            if (gamepad1.a){
                Airplane.setPosition(1);
            }

            if (gamepad2.a){
                if (Pixel_servo.getPosition() != 0){
                    Pixel_servo.setPosition(0);
                } else {
                    Pixel_servo.setPosition(1);
                }
            }
        }
    }
}

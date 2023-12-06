package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import java.util.ArrayList;
import java.util.List;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "AutoHell", group = "Combined")
public class AutoHell extends LinearOpMode {

    private static final boolean USE_WEBCAM = true;

    private TfodProcessor tfod;
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {


        DcMotorEx Motor01 = hardwareMap.get(DcMotorEx.class, "Motor01");
        DcMotorEx Motor02 = hardwareMap.get(DcMotorEx.class, "Motor02");
        DcMotorEx Motor03 = hardwareMap.get(DcMotorEx.class, "Motor03");
        DcMotorEx Motor04 = hardwareMap.get(DcMotorEx.class, "Motor04");

        Motor01.setDirection(DcMotorEx.Direction.FORWARD);
        Motor02.setDirection(DcMotorEx.Direction.REVERSE);
        Motor03.setDirection(DcMotorEx.Direction.REVERSE);
        Motor04.setDirection(DcMotorEx.Direction.FORWARD);

        Motor01.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        Motor02.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        Motor03.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        Motor04.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        Servo Servo_arm1 = hardwareMap.get(Servo.class, "servo_arm1");
        Servo Servo_arm2 = hardwareMap.get(Servo.class, "servo_arm2");
        Servo Servo_hand1 = hardwareMap.get(Servo.class, "servo_hand1");
        Servo Servo_hand2 = hardwareMap.get(Servo.class, "servo_hand2");
        Servo Pixel_servo = hardwareMap.get(Servo.class, "pixel_servo");
        Servo Airplane = hardwareMap.get(Servo.class, "air_servo");

        double velocity_still = 0;
        double velocity_forward = 1400;
        double velocity_backward = -1400;
        double brake = 0;
        double mappedServoPosition;
        double velocity = 1400;
        double still = 0;
        int[] MMotor1 = new int[30];
        int[] MMotor2 = new int[30];
        int[] MMotor3 = new int[30];
        int[] MMotor4 = new int[30];

        int i = 0;
        double x,y,rotate;
        boolean trigger_left;
        boolean trigger_right;
        double range_motor_arm = 0;
        double range_motor_hand = 0;
        boolean Isit_1 = true;
        boolean isMovingToPosition1 = false;
        Motor01.getCurrentPosition();
        Motor03.getCurrentPosition();
        telemetry.addData("Starting at",  "%7d :%7d",
                Motor01.getCurrentPosition(),
                Motor03.getCurrentPosition());

        //telemetry.addData("Going to...", "%7d :%7d"),)
        telemetry.update();



        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {

                if (gamepad1.y)
                    while(true) {
                        { //mapping /w controller
                            x = gamepad1.left_stick_x;
                            y = gamepad1.left_stick_y;
                            rotate = gamepad1.right_stick_x;


                            if (gamepad1.right_bumper) {
                                velocity = 470;
                            }

                            if (gamepad1.left_bumper) {
                                velocity = 1400;
                            }

                            if (x != 1 && y != 1) {
                                Motor01.setVelocity(still);
                                Motor02.setVelocity(still);
                                Motor03.setVelocity(still);
                                Motor04.setVelocity(still);
                            } else if (x != -1 && y != -1) {
                                Motor01.setVelocity(still);
                                Motor02.setVelocity(still);
                                Motor03.setVelocity(still);
                                Motor04.setVelocity(still);
                            }

                            if (y == 1) {
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

                            if (x == 1) {
                                Motor01.setVelocity(velocity);
                                Motor02.setVelocity(velocity);
                                Motor03.setVelocity(velocity);
                                Motor04.setVelocity(velocity);
                            } else if (x == -1) {
                                Motor01.setVelocity(-velocity);
                                Motor02.setVelocity(-velocity);
                                Motor03.setVelocity(-velocity);
                                Motor04.setVelocity(-velocity);
                            }

                            if (rotate == 1) {
                                Motor01.setVelocity(-velocity);
                                Motor02.setVelocity(velocity);
                                Motor03.setVelocity(velocity);
                                Motor04.setVelocity(-velocity);
                            } else if (rotate == -1) {
                                Motor01.setVelocity(velocity);
                                Motor02.setVelocity(-velocity);
                                Motor03.setVelocity(-velocity);
                                Motor04.setVelocity(velocity);
                            }
                            if (gamepad1.x) {
                                //mapping field
                                MMotor1[i] = Motor01.getCurrentPosition(); //input encoder's data into array [i] position
                                MMotor2[i] = Motor02.getCurrentPosition();
                                MMotor3[i] = Motor03.getCurrentPosition();
                                MMotor4[i] = Motor04.getCurrentPosition();
                                i += 1;
                                telemetry.addData("State = ", i);
                                telemetry.update();
                            }
                            if (gamepad1.a)
                            {
                                telemetry.addData("Autonomous Activated ..", "");
                                i = 0;
                                break;
                            }
                        }
                    }
                //main autonomous running from array's data
                Motor01.setTargetPosition(MMotor1[i]);
                Motor02.setTargetPosition(MMotor2[i]);
                Motor03.setTargetPosition(MMotor3[i]);
                Motor04.setTargetPosition(MMotor4[i]);
                i++;
                telemetry.addData("Running State : ", i);
                telemetry.update();
            }



        }

    }
}


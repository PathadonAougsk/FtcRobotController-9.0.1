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
    private DcMotorEx Motor01, Motor02, Motor03, Motor04, GearMotor;
    private Servo Servo_arm1, Servo_arm2, Servo_hand1, Servo_hand2, Pixel_servo, Airplane;
    private double velocity = 1400;
    private final double still = 0;
    private double range_motor_arm = 0;
    private double range_motor_hand = 0;
    private boolean Isit_1 = true;
    private boolean isMovingToPosition1 = false;
    String[] servo_list = {"Arm", "Hand", "Pixel", "Airplane"};
    private long lastServoUpdateTime = 0;
    @Override
    public void runOpMode(){
        initializeHardware();
        waitForStart();
        while (opModeIsActive()){
            // after game start
            if (gamepad1.x && gamepad1.y && gamepad1.dpad_up){
                Debug();
            }

            double x = gamepad1.left_stick_x;
            double y = gamepad1.left_stick_y;
            double rotate = gamepad1.right_stick_x;
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

            if (gamepad1.right_trigger > 0){
                velocity = 466.7;
            }

            if (gamepad1.right_bumper){
                velocity = 1400;
            }

            handleArmMovement();
            if (gamepad1.b){
                PickPixel();
            }

            if (gamepad1.a){
                Prepare_drop();
            }

            Grab_Pixel();

            if (gamepad1.y){
                Airplane.setPosition(1);
            }

            if (gamepad1.left_bumper){
                GearMotor.setVelocity(velocity);
            } else if (gamepad1.left_trigger > 0) {
                GearMotor.setVelocity(-velocity);
            } else {
                GearMotor.setVelocity(still);
            }

            Controll_hand();
            telemetry_output();
        }
    }

    private void initializeHardware(){

        Motor01 = hardwareMap.get(DcMotorEx.class, "Motor01");
        Motor02 = hardwareMap.get(DcMotorEx.class, "Motor02");
        Motor03 = hardwareMap.get(DcMotorEx.class, "Motor03");
        Motor04 = hardwareMap.get(DcMotorEx.class, "Motor04");

        Motor01.setDirection(DcMotor.Direction.FORWARD);
        Motor02.setDirection(DcMotor.Direction.REVERSE);
        Motor03.setDirection(DcMotor.Direction.REVERSE);
        Motor04.setDirection(DcMotor.Direction.FORWARD);

        GearMotor = hardwareMap.get(DcMotorEx.class, "GearMotor");
        GearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Servo_arm1 = hardwareMap.get(Servo.class, "servo_arm1");
        Servo_arm2 = hardwareMap.get(Servo.class, "servo_arm2");
        Servo_hand1 = hardwareMap.get(Servo.class, "servo_hand1");
        Servo_hand2 = hardwareMap.get(Servo.class, "servo_hand2");
        Pixel_servo = hardwareMap.get(Servo.class, "pixel_servo");
        Airplane = hardwareMap.get(Servo.class, "air_servo");

        Servo_arm1.setDirection(Servo.Direction.REVERSE);
        Servo_hand1.setDirection(Servo.Direction.REVERSE);

        Pixel_servo.scaleRange(0, 0.8);
//        Servo_arm1.scaleRange(0, 0.8);
//        Servo_arm2.scaleRange(0, 0.8);
        Pixel_servo.setPosition(1);
    }

    private  void Controll_hand(){
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

    private void handleArmMovement() {

        if (range_motor_arm  > 1){
            range_motor_arm = 1;
        } else if (range_motor_arm < 0){
            range_motor_arm = 0;
        }

        range_motor_arm = (Servo_arm1.getPosition() + (0.025 * gamepad1.right_stick_y));
        Servo_arm1.setPosition(range_motor_arm);
        Servo_arm2.setPosition(range_motor_arm);
    }


    private void PickPixel() {
        Pixel_servo.setPosition(0);
        while (true){
            Servo_arm1.setPosition(Servo_arm1.getPosition() + 0.001);
            Servo_arm2.setPosition(Servo_arm2.getPosition() + 0.001);

            if ((Servo_arm1.getPosition() == 1) && (Servo_arm2.getPosition() == 1))
            {
                break;
            }
        }

        Servo_hand1.setPosition(0.45);
        Servo_hand2.setPosition(0.45);
        sleep(500);
        Pixel_servo.setPosition(1);
    }

    private void Prepare_drop(){
        Servo_arm1.setPosition(0.75);
        Servo_arm2.setPosition(0.75);
        Servo_hand1.setPosition(0.45);
        Servo_hand2.setPosition(0.45);
        Pixel_servo.setPosition(1);
    }

    private void Debug() {
        sleep(5000);
        boolean servo;
        int i = 0; // Initializing index for servo_list

        while (true) {
            telemetry.addData("What are you trying to debug?", "");
            telemetry.addData("X for Servo", "");
            telemetry.addData("Y for Motor", "");
            telemetry.update();

            if (gamepad1.x || gamepad2.x) {
                servo = true;
                break;
            }
            sleep(2000);
        }

        while (true) {
            if (servo) {
                telemetry.addData("Is this what you're trying to debug", servo_list[i]);
                telemetry.addData("Press X to edit / Press Y to continue the list", "");
                telemetry.update();

                sleep(2000);
                if (gamepad1.x || gamepad2.x) {
                    // Call the method to edit servo properties
                    editServo(i);
                    break;
                } else if (gamepad1.y || gamepad2.y) {
                    // Move to the next item in the servo_list
                    i = (i + 1) % servo_list.length; // Cycling through the list
                }
            }
        }
    }

    void editServo(int i) {
        double min = 0;
        double max = 1;
        double rangeIncrement = 0.05;
        double currentEquation;
        Servo edit_servo = null;
        Servo edit_servo_2 = null;

        // Assign servos based on the servo_list
        if (servo_list[i].equalsIgnoreCase("Arm")) {
            edit_servo = Servo_arm1;
            edit_servo_2 = Servo_arm2;
        } else if (servo_list[i].equalsIgnoreCase("Hand")) {
            edit_servo = Servo_hand1;
            edit_servo_2 = Servo_hand2;
            // Implement your logic here for Hand servos
        } else if (servo_list[i].equalsIgnoreCase("Pixel")) {
            edit_servo = Pixel_servo;
            // Implement your logic here for Pixel servo
        } else if (servo_list[i].equalsIgnoreCase("Airplane")) {
            edit_servo = Airplane;
            // Implement your logic here for Airplane servo
        }

        if (edit_servo != null) {
            telemetry.addData("Editing Servo: " + servo_list[i], "");
            telemetry.addData("1. Set Min Range", "");
            telemetry.addData("2. Set Max Range", "");
            telemetry.update();
            sleep(2000);
            while (opModeIsActive()) {
                if (gamepad1.dpad_right || gamepad2.dpad_right) {
                    currentEquation = rangeIncrement;
                } else if (gamepad1.dpad_left || gamepad2.dpad_left) {
                    currentEquation = -rangeIncrement;
                } else {
                    currentEquation = 0;
                }

                if (gamepad1.x || gamepad2.x) {
                    min += currentEquation;
                }

                if (gamepad1.y || gamepad2.y) {
                    max += currentEquation;
                }

                if (min > max) {
                    min = max - rangeIncrement;
                }

                edit_servo.scaleRange(min, max);
                if (edit_servo_2 != null) {
                    edit_servo_2.scaleRange(min, max);
                }

                telemetry.addData("Servo Min Range", min);
                telemetry.addData("Servo Max Range", max);
                telemetry.update();
                telemetry.update();

                if (gamepad1.back || gamepad2.back) {
                    break; // Exit editing mode
                }
            }
        } else {
            // Handle the case where the servo isn't found or assigned
            telemetry.addData("Servo not found!", "");
            telemetry.update();
        }
    }




    private void telemetry_output(){
//         normal mode
//         if something == true
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
        telemetry.addData("Help...", gamepad2.left_stick_y);
        telemetry.update();
        telemetry.update();
    }

    private void Grab_Pixel(){
        if (gamepad1.x) {
            if (Isit_1) {
                if ((Pixel_servo.getPosition() != 0) && (runtime.milliseconds() >= 1500)){
                    Pixel_servo.setPosition(0);
                }
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
                }
            }
        }
    }


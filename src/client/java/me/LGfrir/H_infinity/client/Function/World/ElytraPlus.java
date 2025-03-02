package me.LGfrir.H_infinity.client.Function.World;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig;
import me.LGfrir.H_infinity.client.ConfigOperator.Title;
import me.LGfrir.H_infinity.client.Function.Function;
import me.LGfrir.H_infinity.client.UIDesign.SubUI.SubUI;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import static me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig.ValueType.INTEGER;
import static me.LGfrir.H_infinity.client.ConfigOperator.ParameterConfig.ValueType.FLOAT;
import static me.LGfrir.H_infinity.client.Util.Calculate.*;
import static me.LGfrir.H_infinity.client.Util.Inventory.getStoredItemCount;


public class ElytraPlus extends Function {
    //手部位置
    private int HAND_FIREWORK_ROCKET = 1;
    private int HAND_EXPERIENCE_BOTTLE = 2;
    private int HAND_SHULKER_ROCKET = 3;
    private int HAND_SHULKER_BOTTLE = 4;

    //飞行参数
    private float CLIMB_PITCH = -80f;
    private float CURISE_PITCH = 5.716f;
    private float MIN_CLIMBING_SPEED = 0f;
    private float DIVE_PITCH = 35f;

    //目标坐标值
    private int TAR_X = 0;
    private int TAR_Z = 0;

    //状态
    private int state = 0;

    public ElytraPlus() {


        // 初始化配置列表
        configList.add(new Title("Item Slot"));
        configList.add(new ParameterConfig(
                "Rocket",
                ParameterConfig.ValueType.INTEGER,
                1, 9, 0,
                () -> (double) HAND_FIREWORK_ROCKET,    // Getter：变量 -> 配置
                newValue -> HAND_FIREWORK_ROCKET = newValue.intValue() // Setter：配置 -> 变量
        ));

        configList.add(new ParameterConfig(
                "ExpBottle",
                ParameterConfig.ValueType.INTEGER,
                1, 9, 0,
                () -> (double) HAND_EXPERIENCE_BOTTLE,
                newValue -> HAND_EXPERIENCE_BOTTLE = newValue.intValue()
        ));

        configList.add(new ParameterConfig(
                "ShulkerOfRocket",
                ParameterConfig.ValueType.INTEGER,
                1, 9, 0,
                () -> (double) HAND_SHULKER_ROCKET,
                newValue -> HAND_SHULKER_ROCKET = newValue.intValue()
        ));

        configList.add(new ParameterConfig(
                "ShulkerOfExpBottle",
                ParameterConfig.ValueType.INTEGER,
                1, 9, 0,
                () -> (double) HAND_SHULKER_BOTTLE,
                newValue -> HAND_SHULKER_BOTTLE = newValue.intValue()
        ));

// 飞行参数（浮点型）
        configList.add(new Title("Flying Pitch"));
        configList.add(new ParameterConfig(
                "ClimbPitch",
                ParameterConfig.ValueType.FLOAT,
                -90.0, 90.0, 3,
                () -> (double) CLIMB_PITCH,
                newValue -> CLIMB_PITCH = newValue.floatValue()
        ));

        configList.add(new ParameterConfig(
                "CurisePitch",
                ParameterConfig.ValueType.FLOAT,
                -90.0, 90.0, 3,
                () -> (double) CURISE_PITCH,
                newValue -> CURISE_PITCH = newValue.floatValue()
        ));

        configList.add(new ParameterConfig(
                "MinCimbingSpeed",
                ParameterConfig.ValueType.FLOAT,
                -90, 90, 3,
                () -> (double) MIN_CLIMBING_SPEED,
                newValue -> MIN_CLIMBING_SPEED = newValue.floatValue()
        ));

        configList.add(new ParameterConfig(
                "DivePitch",
                ParameterConfig.ValueType.FLOAT,
                -90, 90, 3,
                () -> (double) DIVE_PITCH,
                newValue -> DIVE_PITCH = newValue.floatValue()
        ));

// 目标坐标（整数型）
        configList.add(new Title("Target Coord"));
        configList.add(new ParameterConfig(
                "TarX",
                ParameterConfig.ValueType.INTEGER,
                -0x3f3f3f3f, 0x3f3f3f3f, 0,
                () -> (double) TAR_X,
                newValue -> TAR_X = newValue.intValue()
        ));

        configList.add(new ParameterConfig(
                "TarZ",
                ParameterConfig.ValueType.INTEGER,
                -0x3f3f3f3f, 0x3f3f3f3f, 0,
                () -> (double) TAR_Z,
                newValue -> TAR_Z = newValue.intValue()
        ));



        subUI = new SubUI(configList);
    }

    @Override
    public void setToggle(boolean toggle) {
        if (!toggle) {
            state = 0;
            client.options.jumpKey.setPressed(false);
            client.options.useKey.setPressed(false);
        }
        this.toggle = toggle;
    }


    /**
     * 执行
     */

    @Override
    public void execute() {
        FlyCondition();
        if (!toggle) return;
        int totalFireworks = 0, totalEXPBottle = 0;

        ItemStack firework = player.getInventory().getStack(HAND_FIREWORK_ROCKET - 1);
        ItemStack EXPBottle = player.getInventory().getStack(HAND_EXPERIENCE_BOTTLE - 1);
        ItemStack shulkerOfFirework = player.getInventory().getStack(HAND_SHULKER_ROCKET - 1);
        ItemStack shulkerOfEXPBottle = player.getInventory().getStack(HAND_SHULKER_BOTTLE - 1);

        totalFireworks += firework.getCount();
        totalEXPBottle += EXPBottle.getCount();
        totalFireworks += getStoredItemCount(shulkerOfFirework, Items.FIREWORK_ROCKET);
        totalEXPBottle += getStoredItemCount(shulkerOfEXPBottle, Items.EXPERIENCE_BOTTLE);

        //player.sendMessage(Text.literal("total fireworks: " + totalFireworks));
        //player.sendMessage(Text.literal("total expBottle: " + totalEXPBottle));

        switch (state++) {
            default:
                System.out.println(state);
            case 0, 3:
                player.getInventory().selectedSlot = HAND_FIREWORK_ROCKET - 1;
                client.options.jumpKey.setPressed(true);
                break;
            case 1, 5:
                break;
            case 2, 4:
                client.options.jumpKey.setPressed(false);
                break;
            case 6:
                client.options.useKey.setPressed(true);
                break;
            case 7:
                client.options.useKey.setPressed(false);
                break;
            case 8:
                player.sendMessage(Text.of("你正在经历爬升阶段"), true);
                climbControl();
                break;
            case 9:
                player.sendMessage(Text.of("你正在经历滑翔阶段"), true);
                curiseControl();
                break;
            case 10:
                player.sendMessage(Text.of("你正在经历降落阶段"), true);
                landingControl();
                break;
            case 11:
                player.sendMessage(Text.of("你正在经历步行阶段"), true);
                walkingControl();
                break;
            case 12:
                stop();
                break;
            case 13:


        }
    }

    private void climbControl() {
        client.player.setYaw(calculateYaw(player.getX(), player.getZ(), TAR_X, TAR_Z));
        client.player.setPitch(CLIMB_PITCH);
        client.options.useKey.setPressed(player.getVelocity().y <= MIN_CLIMBING_SPEED);
        state--;
        if (player.getY() >= 0) state++;
    }

    private void curiseControl() {
        client.player.setPitch(CURISE_PITCH);
        client.player.setYaw(calculateYaw(player.getX(), player.getZ(), TAR_X, TAR_Z));
        if (calculateHorizontalDistance(player.getX(), player.getZ(), TAR_X, TAR_Z) <= 20) {
            if (player.isOnGround())
                state = 11;
            else
                state = 10;
        } else if (player.isOnGround()) state = 0;
        else state--;
    }

    private void landingControl() {
        client.player.setYaw(calculateYaw2(player.getX(), player.getZ(), TAR_X, TAR_Z));
        client.player.setPitch(DIVE_PITCH);
        state--;
        if (player.isOnGround()) state++;
    }

    private void walkingControl() {
        client.player.setYaw(calculateYaw(player.getX(), player.getZ(), TAR_X, TAR_Z));
        client.player.setPitch(0);
        client.options.forwardKey.setPressed(true);
        state--;
        if (calculateHorizontalDistance(player.getX(), player.getZ(), TAR_X, TAR_Z) <= 2) state = 12;
    }

    private void stop() {
        toggle = false;
        client.options.forwardKey.setPressed(false);
        client.player.sendMessage(Text.of("You've reached the destination"));
        state = 0;
        client.options.jumpKey.setPressed(false);
        client.options.useKey.setPressed(false);
    }

    /**
     * 飞行条件判断
     */

    private void FlyCondition() {

        boolean[] Error = new boolean[6];
        String[] ERROR = new String[6];
        ERROR[0] = "you are not holding rocket at slot" + HAND_FIREWORK_ROCKET;
        ERROR[1] = "you are not holding EXPbottle at slot " + HAND_EXPERIENCE_BOTTLE;
        ERROR[2] = "you are not holding a shulker with rocket at slot " + HAND_SHULKER_ROCKET;
        ERROR[3] = "you are not holding a shulker with EXPbottle at slot " + HAND_SHULKER_ROCKET;
        ERROR[4] = "Please make sure that you are in the Nether and standing on solid ground with a Y-coordinate greater than 128";

        ItemStack firework = player.getInventory().getStack(HAND_FIREWORK_ROCKET - 1);
        ItemStack bottle = player.getInventory().getStack(HAND_EXPERIENCE_BOTTLE - 1);
        ItemStack shulkerOfFirework = player.getInventory().getStack(HAND_SHULKER_ROCKET - 1);
        ItemStack shulkerOfEXPBottle = player.getInventory().getStack(HAND_SHULKER_BOTTLE - 1);


        //起飞条件检测
        if (firework.getItem() != Items.FIREWORK_ROCKET) Error[0] = true;
        if (bottle.getItem() != Items.EXPERIENCE_BOTTLE) Error[1] = true;

        if (shulkerOfFirework.getItem() == Items.SHULKER_BOX) {
            Error[2] = getStoredItemCount(shulkerOfFirework, Items.FIREWORK_ROCKET) <= 0;
        } else Error[2] = true;

        if (shulkerOfEXPBottle.getItem() == Items.SHULKER_BOX) {
            Error[2] = getStoredItemCount(shulkerOfEXPBottle, Items.EXPERIENCE_BOTTLE) <= 0;
        } else Error[2] = true;

        if (player.isOnGround() && player.getWorld().getRegistryKey() == World.NETHER && player.getY() >= 128)
            Error[4] = true;


        for (int index = 0; index < Error.length; index++)
            if (Error[index]) {
                player.sendMessage(Text.of(ERROR[index]));
                setToggle(false);
            }

    }


    @Override
    public String getName() {
        return "Elytra+";
    }

    @Override
    public String getDescription() {
        return "Automainti";
    }

    @Override
    public void commandInitialize(LiteralArgumentBuilder<FabricClientCommandSource> command) {

    }


}

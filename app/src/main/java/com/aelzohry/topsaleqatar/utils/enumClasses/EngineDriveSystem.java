package com.aelzohry.topsaleqatar.utils.enumClasses;

import android.text.TextUtils;

import com.aelzohry.topsaleqatar.helper.Helper;
import com.aelzohry.topsaleqatar.model.LocalizedModel;
import com.aelzohry.topsaleqatar.model.StanderModel;

import java.util.ArrayList;

/**
 * # Created by Mousa Hashihso on 02/01/2022.
 */
public enum EngineDriveSystem {

    REAR_WHEEL_DRIVE("rear_wheel_drive", "دفع خلفي", "Rear wheel drive"),
    FOUR_WHEEL_DRIVE("four_wheel_drive", "دفع رباعي", "Four wheel drive"),
    ALL_WHEEL_DRIVE("all_wheel_drive", "دفع كلي مستمر", "All wheel drive"),
    FRONT_PULL("front_pull", "سحب أمامي", "Front pull");

    private String nameAr;
    private String nameEn;
    private String constant;


    EngineDriveSystem(String constant, String nameAr, String nameEn) {
        this.nameAr = nameAr;
        this.nameEn = nameEn;
        this.constant = constant;
    }

    public String getNameAr() {
        return nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getConstant() {
        return constant;
    }

    public static ArrayList<StanderModel> getList() {
        ArrayList<StanderModel> list = new ArrayList<>();
        list.add(new StanderModel(REAR_WHEEL_DRIVE.constant, new LocalizedModel(REAR_WHEEL_DRIVE.nameAr, REAR_WHEEL_DRIVE.nameEn).getLocalized(), false));
        list.add(new StanderModel(FOUR_WHEEL_DRIVE.constant, new LocalizedModel(FOUR_WHEEL_DRIVE.nameAr, FOUR_WHEEL_DRIVE.nameEn).getLocalized(), false));
        list.add(new StanderModel(ALL_WHEEL_DRIVE.constant, new LocalizedModel(ALL_WHEEL_DRIVE.nameAr, ALL_WHEEL_DRIVE.nameEn).getLocalized(), false));
        list.add(new StanderModel(FRONT_PULL.constant, new LocalizedModel(FRONT_PULL.nameAr, FRONT_PULL.nameEn).getLocalized(), false));
        return list;
    }

    public static String getTextByConstant(String constant) {
        if (TextUtils.isEmpty(constant)) return "";

        if (constant.equalsIgnoreCase(REAR_WHEEL_DRIVE.constant)) {
            return Helper.INSTANCE.isEnglish() ? REAR_WHEEL_DRIVE.nameEn : REAR_WHEEL_DRIVE.nameAr;
        } else if (constant.equalsIgnoreCase(FOUR_WHEEL_DRIVE.constant)) {
            return Helper.INSTANCE.isEnglish() ? FOUR_WHEEL_DRIVE.nameEn : FOUR_WHEEL_DRIVE.nameAr;
        } else if (constant.equalsIgnoreCase(ALL_WHEEL_DRIVE.constant)) {
            return Helper.INSTANCE.isEnglish() ? ALL_WHEEL_DRIVE.nameEn : ALL_WHEEL_DRIVE.nameAr;
        } else if (constant.equalsIgnoreCase(FRONT_PULL.constant)) {
            return Helper.INSTANCE.isEnglish() ? FRONT_PULL.nameEn : FRONT_PULL.nameAr;
        }
        return constant;
    }

    public static StanderModel getObjectByConstant(String constant) {

        if (constant.equalsIgnoreCase(REAR_WHEEL_DRIVE.constant)) {
            return new StanderModel(REAR_WHEEL_DRIVE.constant, new LocalizedModel(REAR_WHEEL_DRIVE.nameAr, REAR_WHEEL_DRIVE.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FOUR_WHEEL_DRIVE.constant)) {
            return new StanderModel(FOUR_WHEEL_DRIVE.constant, new LocalizedModel(FOUR_WHEEL_DRIVE.nameAr, FOUR_WHEEL_DRIVE.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(ALL_WHEEL_DRIVE.constant)) {
            return new StanderModel(ALL_WHEEL_DRIVE.constant, new LocalizedModel(ALL_WHEEL_DRIVE.nameAr, ALL_WHEEL_DRIVE.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FRONT_PULL.constant)) {
            return new StanderModel(FRONT_PULL.constant, new LocalizedModel(FRONT_PULL.nameAr, FRONT_PULL.nameEn).getLocalized(), false);
        }
        return null;
    }


}
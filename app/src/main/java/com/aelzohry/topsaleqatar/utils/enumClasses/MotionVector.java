package com.aelzohry.topsaleqatar.utils.enumClasses;

import android.text.TextUtils;

import com.aelzohry.topsaleqatar.helper.Helper;
import com.aelzohry.topsaleqatar.model.LocalizedModel;
import com.aelzohry.topsaleqatar.model.StanderModel;

import java.util.ArrayList;

/**
 * # Created by Mousa Hashihso on 02/01/2022.
 */
public enum MotionVector {


    AUTOMATIC("automatic", "أوتوماتيك", "Automatic"),
    STEPTRONIC("steptronic", "ستيبترونيك", "Steptronic"),
    MANUAL("manual", "يدوي", "Manual"),
    F_1("f_1", "اف -1", "F-1"),
    SMG("smg", "اس ام جي", "SMG"),
    OTHER("electrical", "كهربائي", "Electrical");


    private String nameAr;
    private String nameEn;
    private String constant;


    MotionVector(String constant, String nameAr, String nameEn) {
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
        list.add(new StanderModel(AUTOMATIC.constant, new LocalizedModel(AUTOMATIC.nameAr, AUTOMATIC.nameEn).getLocalized(), false));
        list.add(new StanderModel(STEPTRONIC.constant, new LocalizedModel(STEPTRONIC.nameAr, STEPTRONIC.nameEn).getLocalized(), false));
        list.add(new StanderModel(MANUAL.constant, new LocalizedModel(MANUAL.nameAr, MANUAL.nameEn).getLocalized(), false));
        list.add(new StanderModel(F_1.constant, new LocalizedModel(F_1.nameAr, F_1.nameEn).getLocalized(), false));
        list.add(new StanderModel(SMG.constant, new LocalizedModel(SMG.nameAr, SMG.nameEn).getLocalized(), false));
        list.add(new StanderModel(OTHER.constant, new LocalizedModel(OTHER.nameAr, OTHER.nameEn).getLocalized(), false));
        return list;
    }

    public static String getTextByConstant(String constant) {
        if (TextUtils.isEmpty(constant)) return "";

        if (constant.equalsIgnoreCase(AUTOMATIC.constant)) {
            return Helper.INSTANCE.isEnglish() ? AUTOMATIC.nameEn : AUTOMATIC.nameAr;
        } else if (constant.equalsIgnoreCase(STEPTRONIC.constant)) {
            return Helper.INSTANCE.isEnglish() ? STEPTRONIC.nameEn : STEPTRONIC.nameAr;
        } else if (constant.equalsIgnoreCase(MANUAL.constant)) {
            return Helper.INSTANCE.isEnglish() ? MANUAL.nameEn : MANUAL.nameAr;
        } else if (constant.equalsIgnoreCase(F_1.constant)) {
            return Helper.INSTANCE.isEnglish() ? F_1.nameEn : F_1.nameAr;
        } else if (constant.equalsIgnoreCase(SMG.constant)) {
            return Helper.INSTANCE.isEnglish() ? SMG.nameEn : SMG.nameAr;
        } else if (constant.equalsIgnoreCase(OTHER.constant)) {
            return Helper.INSTANCE.isEnglish() ? OTHER.nameEn : OTHER.nameAr;
        }
        return constant;
    }

    public static StanderModel getObjectByConstant(String constant) {

        if (constant.equalsIgnoreCase(AUTOMATIC.constant)) {
            return new StanderModel(AUTOMATIC.constant, new LocalizedModel(AUTOMATIC.nameAr, AUTOMATIC.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(STEPTRONIC.constant)) {
            return new StanderModel(STEPTRONIC.constant, new LocalizedModel(STEPTRONIC.nameAr, STEPTRONIC.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(MANUAL.constant)) {
            return new StanderModel(MANUAL.constant, new LocalizedModel(MANUAL.nameAr, MANUAL.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(F_1.constant)) {
            return new StanderModel(F_1.constant, new LocalizedModel(F_1.nameAr, F_1.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(SMG.constant)) {
            return new StanderModel(SMG.constant, new LocalizedModel(SMG.nameAr, SMG.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(OTHER.constant)) {
            return new StanderModel(OTHER.constant, new LocalizedModel(OTHER.nameAr, OTHER.nameEn).getLocalized(), false);
        }
        return null;
    }


}
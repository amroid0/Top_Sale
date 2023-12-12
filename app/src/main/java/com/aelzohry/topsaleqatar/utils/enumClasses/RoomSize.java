package com.aelzohry.topsaleqatar.utils.enumClasses;

import android.text.TextUtils;

import com.aelzohry.topsaleqatar.helper.Helper;
import com.aelzohry.topsaleqatar.model.LocalizedModel;
import com.aelzohry.topsaleqatar.model.StanderModel;

import java.util.ArrayList;

/**
 * # Created by Mousa Hashihso on 02/01/2022.
 */
public enum RoomSize {

//
//    ONE("one", "1", "1"),
//    TWO("two", "2", "2"),
//    THREE("three", "3", "3"),
//    FOUR("four", "4", "4"),
//    FIVE("five", "5", "5"),
//    OTHER("other", "6 فأكثر", "More than 6");

    ZERO("zero", "0", "0"),
    ONE("1", "1", "1"),
    TWO("2", "2", "2"),
    THREE("3", "3", "3"),
    FOUR("4", "4", "4"),
    FIVE("5", "5", "5"),
    OTHER("6", "أكثر من 6", "More than 6");


    private String nameAr;
    private String nameEn;
    private String constant;


    RoomSize(String constant, String nameAr, String nameEn) {
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
        list.add(new StanderModel(ZERO.constant, new LocalizedModel(ZERO.nameAr, ZERO.nameEn).getLocalized(), false));
        list.add(new StanderModel(ONE.constant, new LocalizedModel(ONE.nameAr, ONE.nameEn).getLocalized(), false));
        list.add(new StanderModel(TWO.constant, new LocalizedModel(TWO.nameAr, TWO.nameEn).getLocalized(), false));
        list.add(new StanderModel(THREE.constant, new LocalizedModel(THREE.nameAr, THREE.nameEn).getLocalized(), false));
        list.add(new StanderModel(FOUR.constant, new LocalizedModel(FOUR.nameAr, FOUR.nameEn).getLocalized(), false));
        list.add(new StanderModel(FIVE.constant, new LocalizedModel(FIVE.nameAr, FIVE.nameEn).getLocalized(), false));
        list.add(new StanderModel(OTHER.constant, new LocalizedModel(OTHER.nameAr, OTHER.nameEn).getLocalized(), false));
        return list;
    }

    public static String getTextByConstant(String constant) {
        if (TextUtils.isEmpty(constant)) return "";

        if (constant.equalsIgnoreCase(ZERO.constant)) {
            return Helper.INSTANCE.isEnglish() ? ZERO.nameEn : ZERO.nameAr;
        } else if (constant.equalsIgnoreCase(ONE.constant)) {
            return Helper.INSTANCE.isEnglish() ? ONE.nameEn : ONE.nameAr;
        } else if (constant.equalsIgnoreCase(TWO.constant)) {
            return Helper.INSTANCE.isEnglish() ? TWO.nameEn : TWO.nameAr;
        } else if (constant.equalsIgnoreCase(THREE.constant)) {
            return Helper.INSTANCE.isEnglish() ? THREE.nameEn : THREE.nameAr;
        } else if (constant.equalsIgnoreCase(FOUR.constant)) {
            return Helper.INSTANCE.isEnglish() ? FOUR.nameEn : FOUR.nameAr;
        } else if (constant.equalsIgnoreCase(FIVE.constant)) {
            return Helper.INSTANCE.isEnglish() ? FIVE.nameEn : FIVE.nameAr;
        } else if (constant.equalsIgnoreCase(OTHER.constant)) {
            return Helper.INSTANCE.isEnglish() ? OTHER.nameEn : OTHER.nameAr;
        }
        return constant;
    }


    public static StanderModel getObjectByConstant(String constant) {

        if (constant.equalsIgnoreCase(ZERO.constant)) {
            return new StanderModel(ZERO.constant, new LocalizedModel(ZERO.nameAr, ZERO.nameEn).getLocalized(), false);

        } else if (constant.equalsIgnoreCase(ONE.constant)) {
            return new StanderModel(ONE.constant, new LocalizedModel(ONE.nameAr, ONE.nameEn).getLocalized(), false);

        } else if (constant.equalsIgnoreCase(TWO.constant)) {
            return new StanderModel(TWO.constant, new LocalizedModel(TWO.nameAr, TWO.nameEn).getLocalized(), false);

        } else if (constant.equalsIgnoreCase(THREE.constant)) {
            return new StanderModel(THREE.constant, new LocalizedModel(THREE.nameAr, THREE.nameEn).getLocalized(), false);

        } else if (constant.equalsIgnoreCase(FOUR.constant)) {
            return new StanderModel(FOUR.constant, new LocalizedModel(FOUR.nameAr, FOUR.nameEn).getLocalized(), false);

        } else if (constant.equalsIgnoreCase(FIVE.constant)) {
            return new StanderModel(FIVE.constant, new LocalizedModel(FIVE.nameAr, FIVE.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(OTHER.constant)) {
            return new StanderModel(OTHER.constant, new LocalizedModel(OTHER.nameAr, OTHER.nameEn).getLocalized(), false);

        }
        return null;
    }


}
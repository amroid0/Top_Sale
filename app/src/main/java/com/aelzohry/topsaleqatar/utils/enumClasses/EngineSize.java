package com.aelzohry.topsaleqatar.utils.enumClasses;

import android.text.TextUtils;

import com.aelzohry.topsaleqatar.helper.Helper;
import com.aelzohry.topsaleqatar.model.LocalizedModel;
import com.aelzohry.topsaleqatar.model.StanderModel;

import java.util.ArrayList;

/**
 * # Created by Mousa Hashihso on 02/01/2022.
 */
public enum EngineSize {


    ZERO("0", "0", "0"),
    THREE("3", "3", "3"),
    FOUR("4", "4", "4"),
    FIVE("5", "5", "5"),
    SIX("6", "6", "6"),
    EIGHT("8", "8", "8"),
    TEN("10", "10", "10"),
    TWELVE("12", "12", "12"),
    SIXTEEN("16", "16", "16"),
    OTHER("electrical", "كهربائي", "Electrical");


    private String nameAr;
    private String nameEn;
    private String constant;


    EngineSize(String constant, String nameAr, String nameEn) {
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
    static ArrayList<StanderModel> list = new ArrayList<>();

    public static ArrayList<StanderModel> getList() {
       return list;
    }
    public static void populate(){
        list.add(new StanderModel(ZERO.constant, new LocalizedModel(ZERO.nameAr, ZERO.nameEn).getLocalized(), false));
        list.add(new StanderModel(THREE.constant, new LocalizedModel(THREE.nameAr, THREE.nameEn).getLocalized(), false));
        list.add(new StanderModel(FOUR.constant, new LocalizedModel(FOUR.nameAr, FOUR.nameEn).getLocalized(), false));
        list.add(new StanderModel(FIVE.constant, new LocalizedModel(FIVE.nameAr, FIVE.nameEn).getLocalized(), false));
        list.add(new StanderModel(SIX.constant, new LocalizedModel(SIX.nameAr, SIX.nameEn).getLocalized(), false));
        list.add(new StanderModel(EIGHT.constant, new LocalizedModel(EIGHT.nameAr, EIGHT.nameEn).getLocalized(), false));
        list.add(new StanderModel(TEN.constant, new LocalizedModel(TEN.nameAr, TEN.nameEn).getLocalized(), false));
        list.add(new StanderModel(TWELVE.constant, new LocalizedModel(TWELVE.nameAr, TWELVE.nameEn).getLocalized(), false));
        list.add(new StanderModel(SIXTEEN.constant, new LocalizedModel(SIXTEEN.nameAr, SIXTEEN.nameEn).getLocalized(), false));
        list.add(new StanderModel(OTHER.constant, new LocalizedModel(OTHER.nameAr, OTHER.nameEn).getLocalized(), false));
    }

    public static String getTextByConstant(String constant) {
        if (TextUtils.isEmpty(constant)) return "";

        if (constant.equalsIgnoreCase(ZERO.constant)) {
            return Helper.INSTANCE.isEnglish() ? ZERO.nameEn : ZERO.nameAr;
        } else if (constant.equalsIgnoreCase(THREE.constant)) {
            return Helper.INSTANCE.isEnglish() ? THREE.nameEn : THREE.nameAr;
        } else if (constant.equalsIgnoreCase(FOUR.constant)) {
            return Helper.INSTANCE.isEnglish() ? FOUR.nameEn : FOUR.nameAr;
        } else if (constant.equalsIgnoreCase(FIVE.constant)) {
            return Helper.INSTANCE.isEnglish() ? FIVE.nameEn : FIVE.nameAr;
        } else if (constant.equalsIgnoreCase(SIX.constant)) {
            return Helper.INSTANCE.isEnglish() ? SIX.nameEn : SIX.nameAr;
        } else if (constant.equalsIgnoreCase(EIGHT.constant)) {
            return Helper.INSTANCE.isEnglish() ? EIGHT.nameEn : EIGHT.nameAr;
        } else if (constant.equalsIgnoreCase(TEN.constant)) {
            return Helper.INSTANCE.isEnglish() ? TEN.nameEn : TEN.nameAr;
        } else if (constant.equalsIgnoreCase(TWELVE.constant)) {
            return Helper.INSTANCE.isEnglish() ? TWELVE.nameEn : TWELVE.nameAr;
        } else if (constant.equalsIgnoreCase(SIXTEEN.constant)) {
            return Helper.INSTANCE.isEnglish() ? SIXTEEN.nameEn : SIXTEEN.nameAr;
        } else if (constant.equalsIgnoreCase(OTHER.constant)) {
            return Helper.INSTANCE.isEnglish() ? OTHER.nameEn : OTHER.nameAr;
        }
        return constant;
    }


    public static StanderModel getObjectByConstant(String constant) {

        if (constant.equalsIgnoreCase(ZERO.constant)) {
            return new StanderModel(ZERO.constant, new LocalizedModel(ZERO.nameAr, ZERO.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(THREE.constant)) {
            return new StanderModel(THREE.constant, new LocalizedModel(THREE.nameAr, THREE.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FOUR.constant)) {
            return new StanderModel(FOUR.constant, new LocalizedModel(FOUR.nameAr, FOUR.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FIVE.constant)) {
            return new StanderModel(FIVE.constant, new LocalizedModel(FIVE.nameAr, FIVE.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(SIX.constant)) {
            return new StanderModel(SIX.constant, new LocalizedModel(SIX.nameAr, SIX.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(EIGHT.constant)) {
            return new StanderModel(EIGHT.constant, new LocalizedModel(EIGHT.nameAr, EIGHT.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(TEN.constant)) {
            return new StanderModel(TEN.constant, new LocalizedModel(TEN.nameAr, TEN.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(TWELVE.constant)) {
            return new StanderModel(TWELVE.constant, new LocalizedModel(TWELVE.nameAr, TWELVE.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(SIXTEEN.constant)) {
            return new StanderModel(SIXTEEN.constant, new LocalizedModel(SIXTEEN.nameAr, SIXTEEN.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(OTHER.constant)) {
            return new StanderModel(OTHER.constant, new LocalizedModel(OTHER.nameAr, OTHER.nameEn).getLocalized(), false);
        }
        return null;
    }


}
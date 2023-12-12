package com.aelzohry.topsaleqatar.utils.enumClasses;

import android.text.TextUtils;

import com.aelzohry.topsaleqatar.helper.Helper;
import com.aelzohry.topsaleqatar.model.LocalizedModel;
import com.aelzohry.topsaleqatar.model.StanderModel;

import java.util.ArrayList;

/**
 * # Created by Mousa Hashihso on 02/01/2022.
 */
public enum FuelType {

    PETROL("petrol", "بنزين", "Petrol"),
    DIESEL("diesel", "ديزل", "Diesel"),
    HYBRID("hybrid", "هايبرد", "Hybrid"),
    ELECTRIC("electric", "كهربائية", "Electric");


    private String nameAr;
    private String nameEn;
    private String constant;


    FuelType(String constant, String nameAr, String nameEn) {
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
        list.add(new StanderModel(PETROL.constant, new LocalizedModel(PETROL.nameAr, PETROL.nameEn).getLocalized(), false));
        list.add(new StanderModel(DIESEL.constant, new LocalizedModel(DIESEL.nameAr, DIESEL.nameEn).getLocalized(), false));
        list.add(new StanderModel(HYBRID.constant, new LocalizedModel(HYBRID.nameAr, HYBRID.nameEn).getLocalized(), false));
        list.add(new StanderModel(ELECTRIC.constant, new LocalizedModel(ELECTRIC.nameAr, ELECTRIC.nameEn).getLocalized(), false));
        return list;
    }

    public static String getTextByConstant(String constant) {
        if (TextUtils.isEmpty(constant)) return "";

        if (constant.equalsIgnoreCase(PETROL.constant)) {
            return Helper.INSTANCE.isEnglish() ? PETROL.nameEn : PETROL.nameAr;
        } else if (constant.equalsIgnoreCase(DIESEL.constant)) {
            return Helper.INSTANCE.isEnglish() ? DIESEL.nameEn : DIESEL.nameAr;
        } else if (constant.equalsIgnoreCase(HYBRID.constant)) {
            return Helper.INSTANCE.isEnglish() ? HYBRID.nameEn : HYBRID.nameAr;
        } else if (constant.equalsIgnoreCase(ELECTRIC.constant)) {
            return Helper.INSTANCE.isEnglish() ? ELECTRIC.nameEn : ELECTRIC.nameAr;
        }
        return constant;
    }

    public static StanderModel getObjectByConstant(String constant) {

        if (constant.equalsIgnoreCase(PETROL.constant)) {
            return new StanderModel(PETROL.constant, new LocalizedModel(PETROL.nameAr, PETROL.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(DIESEL.constant)) {
            return new StanderModel(DIESEL.constant, new LocalizedModel(DIESEL.nameAr, DIESEL.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(HYBRID.constant)) {
            return new StanderModel(HYBRID.constant, new LocalizedModel(HYBRID.nameAr, HYBRID.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(ELECTRIC.constant)) {
            return new StanderModel(ELECTRIC.constant, new LocalizedModel(ELECTRIC.nameAr, ELECTRIC.nameEn).getLocalized(), false);
        }
        return null;
    }


}
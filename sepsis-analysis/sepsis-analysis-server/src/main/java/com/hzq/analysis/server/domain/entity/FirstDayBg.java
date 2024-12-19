package com.hzq.analysis.server.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.entity FirstDayBg
 * @date 2024/12/13 21:30
 * @description 入ICU第一天血气分析结果实体类
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "first_day_bg")
public class FirstDayBg extends BaseEntity {

    @Column(name = "ph_min", columnDefinition = " comment '动脉血气ph值(-)'")
    private double phMin;

    @Column(name = "ph_max", columnDefinition = " comment '动脉血气ph值(-)'")
    private double phMax;

    @Column(name = "ph", columnDefinition = " comment '动脉血气ph值(-)'")
    private double ph;

    @Column(name = "po2_min", columnDefinition = " comment '动脉血气氧分压(mmHg)'")
    private double po2Min;

    @Column(name = "po2_max", columnDefinition = " comment '动脉血气氧分压(mmHg)'")
    private double po2Max;

    @Column(name = "po2", columnDefinition = " comment '动脉血气氧分压(mmHg)'")
    private double po2;

    @Column(name = "paco2_min", columnDefinition = " comment '动脉血气二氧化碳分压(mmHg)'")
    private double paco2Min;

    @Column(name = "paco2_max", columnDefinition = " comment '动脉血气二氧化碳分压(mmHg)'")
    private double paco2Max;

    @Column(name = "paco2", columnDefinition = " comment '动脉血气二氧化碳分压(mmHg)'")
    private double paco2;

    @Column(name = "lac_min", columnDefinition = " comment '动脉血气乳酸(mmol/L)'")
    private double lacMin;

    @Column(name = "lac_max", columnDefinition = " comment '动脉血气乳酸(mmol/L)'")
    private double lacMax;

    @Column(name = "lac", columnDefinition = " comment '动脉血气乳酸(mmol/L)'")
    private double lac;

    @Column(name = "pao2_fio2_min", columnDefinition = " comment 'PaO2/FiO2比率(-)'")
    private double pao2Fio2Min;

    @Column(name = "pao2_fio2_max", columnDefinition = " comment 'PaO2/FiO2比率(-)'")
    private double pao2Fio2Max;

    @Column(name = "pao2_fio2", columnDefinition = " comment 'PaO2/FiO2比率(-)'")
    private double pao2Fio2;

    @Column(name = "be_min", columnDefinition = " comment '血液酸碱平衡(-)'")
    private double beMin;

    @Column(name = "be_max", columnDefinition = " comment '血液酸碱平衡(-)'")
    private double beMax;

    @Column(name = "be", columnDefinition = " comment '血液酸碱平衡(-)'")
    private double be;

    @Column(name = "total_co2_min", columnDefinition = " comment '血液总CO2量(mmol/L)'")
    private double totalCo2Min;

    @Column(name = "total_co2_max", columnDefinition = " comment '血液总CO2量(mmol/L)'")
    private double totalCo2Max;

    @Column(name = "total_co2", columnDefinition = " comment '血液总CO2量(mmol/L)'")
    private double totalCo2;

    @Column(name = "hematocrit_min", columnDefinition = " comment '红细胞压积(%)'")
    private double hematocritMin;

    @Column(name = "hematocrit_max", columnDefinition = " comment '红细胞压积(%)'")
    private double hematocritMax;

    @Column(name = "hematocrit", columnDefinition = " comment '红细胞压积(%)'")
    private double hematocrit;

    @Column(name = "hemoglobin_min", columnDefinition = " comment '血红蛋白水平(%)'")
    private double hemoglobinMin;

    @Column(name = "hemoglobin_max", columnDefinition = " comment '血红蛋白水平(%)'")
    private double hemoglobinMax;

    @Column(name = "hemoglobin", columnDefinition = " comment '血红蛋白水平(%)'")
    private double hemoglobin;

    @Column(name = "carboxyhemoglobin_min", columnDefinition = " comment '羧血红蛋白(%)'")
    private double carboxyhemoglobinMin;

    @Column(name = "carboxyhemoglobin_max", columnDefinition = " comment '羧血红蛋白(%)'")
    private double carboxyhemoglobinMax;

    @Column(name = "carboxyhemoglobin", columnDefinition = " comment '羧血红蛋白(%)'")
    private double carboxyhemoglobin;

    @Column(name = "methemoglobin_min", columnDefinition = " comment '甲血红蛋白(%)'")
    private double methemoglobinMin;

    @Column(name = "methemoglobin_max", columnDefinition = " comment '甲血红蛋白(%)'")
    private double methemoglobinMax;

    @Column(name = "methemoglobin", columnDefinition = " comment '甲血红蛋白(%)'")
    private double methemoglobin;

    @Column(name = "cl_min", columnDefinition = " comment '血清氯(mmol/L)'")
    private double clMin;

    @Column(name = "cl_max", columnDefinition = " comment '血清氯(mmol/L)'")
    private double clMax;

    @Column(name = "cl", columnDefinition = " comment '血清氯(mmol/L)'")
    private double cl;

    @Column(name = "calcium_min", columnDefinition = " comment '血清钙(mmol/L)'")
    private double calciumMin;

    @Column(name = "calcium_max", columnDefinition = " comment '血清钙(mmol/L)'")
    private double calciumMax;

    @Column(name = "calcium", columnDefinition = " comment '血清钙(mmol/L)'")
    private double calcium;

    @Column(name = "sodium_min", columnDefinition = " comment '血清钠(mmol/L)'")
    private double sodiumMin;

    @Column(name = "sodium_max", columnDefinition = " comment '血清钠(mmol/L)'")
    private double sodiumMax;

    @Column(name = "sodium", columnDefinition = " comment '血清钠(mmol/L)'")
    private double sodium;

    @Column(name = "potassium_min", columnDefinition = " comment '血清钾(mmol/L)'")
    private double potassiumMin;

    @Column(name = "potassium_max", columnDefinition = " comment '血清钾(mmol/L)'")
    private double potassiumMax;

    @Column(name = "potassium", columnDefinition = " comment '血清钾(mmol/L)'")
    private double potassium;

    @Column(name = "glucose_min", columnDefinition = " comment '葡萄糖含量(mmol/L)'")
    private double glucoseMin;

    @Column(name = "glucose_max", columnDefinition = " comment '葡萄糖含量(mmol/L)'")
    private double glucoseMax;

    @Column(name = "glucose", columnDefinition = " comment '葡萄糖含量(mmol/L)'")
    private double glucose;

    @Column(name = "plt_min", columnDefinition = " comment '血小板计数(10^9/L)'")
    private double pltMin;

    @Column(name = "plt_max", columnDefinition = " comment '血小板计数(10^9/L)'")
    private double pltMax;

    @Column(name = "plt", columnDefinition = " comment '血小板计数(10^9/L)'")
    private double plt;

    @Column(name = "wbc_min", columnDefinition = " comment '白细胞计数(10^9/L)'")
    private double wbcMin;

    @Column(name = "wbc_max", columnDefinition = " comment '白细胞计数(10^9/L)'")
    private double wbcMax;

    @Column(name = "wbc", columnDefinition = " comment '白细胞计数(10^9/L)'")
    private double wbc;

    @Column(name = "lymp_count_min", columnDefinition = " comment '淋巴细胞计数(10^9/L)'")
    private double lympCountMin;

    @Column(name = "lymp_count_max", columnDefinition = " comment '淋巴细胞计数(10^9/L)'")
    private double lympCountMax;

    @Column(name = "lymp_count", columnDefinition = " comment '淋巴细胞计数(10^9/L)'")
    private double lympCount;

    @Column(name = "monocytes_min", columnDefinition = " comment '单核细胞计数(10^9/L)'")
    private double monocytesMin;

    @Column(name = "monocytes_max", columnDefinition = " comment '单核细胞计数(10^9/L)'")
    private double monocytesMax;

    @Column(name = "monocytes", columnDefinition = " comment '单核细胞计数(10^9/L)'")
    private double monocytes;

    @Column(name = "eosinophils_min", columnDefinition = " comment '嗜碱粒细胞计数(10^9/L)'")
    private double eosinophilsMin;

    @Column(name = "eosinophils_max", columnDefinition = " comment '嗜碱粒细胞计数(10^9/L)'")
    private double eosinophilsMax;

    @Column(name = "eosinophils", columnDefinition = " comment '嗜碱粒细胞计数(10^9/L)'")
    private double eosinophils;

    @Column(name = "basophils_min", columnDefinition = " comment '嗜酸粒细胞计数(10^9/L)'")
    private double basophilsMin;

    @Column(name = "basophils_max", columnDefinition = " comment '嗜酸粒细胞计数(10^9/L)'")
    private double basophilsMax;

    @Column(name = "basophils", columnDefinition = " comment '嗜酸粒细胞计数(10^9/L)'")
    private double basophils;

    @Column(name = "neutrophils_min", columnDefinition = " comment '中性粒细胞计数(10^9/L)'")
    private double neutrophilsMin;

    @Column(name = "neutrophils_max", columnDefinition = " comment '中性粒细胞计数(10^9/L)'")
    private double neutrophilsMax;

    @Column(name = "neutrophils", columnDefinition = " comment '中性粒细胞计数(10^9/L)'")
    private double neutrophils;

    @Column(name = "alb_min", columnDefinition = " comment '血清白蛋白浓度(g/L)'")
    private double albMin;

    @Column(name = "alb_max", columnDefinition = " comment '血清白蛋白浓度(g/L)'")
    private double albMax;

    @Column(name = "alb", columnDefinition = " comment '血清白蛋白浓度(g/L)'")
    private double alb;

    @Column(name = "aniongap_min", columnDefinition = " comment '阴离子间隙(mmol/L)'")
    private double aniongapMin;

    @Column(name = "aniongap_max", columnDefinition = " comment '阴离子间隙(mmol/L)'")
    private double aniongapMax;

    @Column(name = "aniongap", columnDefinition = " comment '阴离子间隙(mmol/L)'")
    private double aniongap;

    @Column(name = "bun_min", columnDefinition = " comment '尿素氮含量(mmol/L)'")
    private double bunMin;

    @Column(name = "bun_max", columnDefinition = " comment '尿素氮含量(mmol/L)'")
    private double bunMax;

    @Column(name = "bun", columnDefinition = " comment '尿素氮含量(mmol/L)'")
    private double bun;

    @Column(name = "cr_min", columnDefinition = " comment '血肌酐(μmol/L)'")
    private double crMin;

    @Column(name = "cr_max", columnDefinition = " comment '血肌酐(μmol/L)'")
    private double crMax;

    @Column(name = "cr", columnDefinition = " comment '血肌酐(μmol/L)'")
    private double cr;

    @Column(name = "fibrinogen_min", columnDefinition = " comment '纤维蛋白原含量(g/L)'")
    private double fibrinogenMin;

    @Column(name = "fibrinogen_max", columnDefinition = " comment '纤维蛋白原含量(g/L)'")
    private double fibrinogenMax;

    @Column(name = "fibrinogen", columnDefinition = " comment '纤维蛋白原含量(g/L)'")
    private double fibrinogen;

    @Column(name = "alt_min", columnDefinition = " comment '丙氨酸氨基转移酶(U/L)'")
    private double altMin;

    @Column(name = "alt_max", columnDefinition = " comment '丙氨酸氨基转移酶(U/L)'")
    private double altMax;

    @Column(name = "alt", columnDefinition = " comment '丙氨酸氨基转移酶(U/L)'")
    private double alt;

    @Column(name = "alp_min", columnDefinition = " comment '碱性磷酸酶(U/L)'")
    private double alpMin;

    @Column(name = "alp_max", columnDefinition = " comment '碱性磷酸酶(U/L)'")
    private double alpMax;

    @Column(name = "alp", columnDefinition = " comment '碱性磷酸酶(U/L)'")
    private double alp;

    @Column(name = "ast_min", columnDefinition = " comment '天冬氨酸氨基转移酶(U/L)'")
    private double astMin;

    @Column(name = "ast_max", columnDefinition = " comment '天冬氨酸氨基转移酶(U/L)'")
    private double astMax;

    @Column(name = "ast", columnDefinition = " comment '天冬氨酸氨基转移酶(U/L)'")
    private double ast;

    @Column(name = "ck_min", columnDefinition = " comment '肌酸激酶(U/L)'")
    private double ckMin;

    @Column(name = "ck_max", columnDefinition = " comment '肌酸激酶(U/L)'")
    private double ckMax;

    @Column(name = "ck", columnDefinition = " comment '肌酸激酶(U/L)'")
    private double ck;

    @Column(name = "ld_min", columnDefinition = " comment '乳酸脱氢酶(U/L)'")
    private double ldMin;

    @Column(name = "ld_max", columnDefinition = " comment '乳酸脱氢酶(U/L)'")
    private double ldMax;

    @Column(name = "ld", columnDefinition = " comment '乳酸脱氢酶(U/L)'")
    private double ld;

    @Column(name = "bilirubin_total_min", columnDefinition = " comment '总胆红素(μmol/L)'")
    private double bilirubinTotalMin;

    @Column(name = "bilirubin_total_max", columnDefinition = " comment '总胆红素(μmol/L)'")
    private double bilirubinTotalMax;

    @Column(name = "bilirubin_total", columnDefinition = " comment '总胆红素(μmol/L)'")
    private double bilirubinTotal;

    @Column(name = "pt_min", columnDefinition = " comment '凝血酶原时间(s)'")
    private double ptMin;

    @Column(name = "pt_max", columnDefinition = " comment '凝血酶原时间(s)'")
    private double ptMax;

    @Column(name = "pt", columnDefinition = " comment '凝血酶原时间(s)'")
    private double pt;

    @Column(name = "ptt_min", columnDefinition = " comment '部分凝血活酶时间(s)'")
    private double pttMin;

    @Column(name = "ptt_max", columnDefinition = " comment '部分凝血活酶时间(s)'")
    private double pttMax;

    @Column(name = "ptt", columnDefinition = " comment '部分凝血活酶时间(s)'")
    private double ptt;

    @Column(name = "inr_min", columnDefinition = " comment '国际标准化比值(-)'")
    private double inrMin;

    @Column(name = "inr_max", columnDefinition = " comment '国际标准化比值(-)'")
    private double inrMax;

    @Column(name = "inr", columnDefinition = " comment '国际标准化比值(-)'")
    private double inr;
}

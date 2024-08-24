package ru.boomearo.langhelper.versions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Перечисление всех возможных поддерживаемых переводов игры Minecraft.
 */
@Getter
@RequiredArgsConstructor
public enum LangType {

    //Только английский язык является не скачиваемым, поэтому переопределяем ему этот метод
    EN_US() {
        @Override
        public boolean isExternal() {
            return false;
        }

    },
    VEC_IT,
    UK_UA,
    VI_VN,
    EN_UD,
    LA_LA,
    NDS_DE,
    HR_HR,
    LT_LT,
    GD_GB,
    QYA_AA,
    FIL_PH,
    NL_NL,
    HE_IL,
    JBO_EN,
    MN_MN,
    FR_FR,
    EN_GB,
    KSH_DE,
    FR_CA,
    HAW_US,
    GV_IM,
    ES_ES,
    GA_IE,
    DE_ALG,
    IG_NG,
    EN_PT,
    BE_BY,
    EN_AU,
    IT_IT,
    AR_SA,
    DE_AT,
    GL_ES,
    OJ_CA,
    EL_GR,
    KN_IN,
    CS_CZ,
    BG_BG,
    TLH_AA,
    HI_IN,
    LI_LI,
    IO_EN,
    KAB_KAB,
    ES_MX,
    EU_ES,
    TZL_TZL,
    PL_PL,
    KA_GE,
    ES_AR,
    KW_GB,
    SE_NO,
    HY_AM,
    BS_BA,
    EN_CA,
    TH_TH,
    TR_TR,
    FI_FI,
    NL_BE,
    SQ_AL,
    DA_DK,
    EO_UY,
    IS_IS,
    SV_SE,
    FO_FO,
    MI_NZ,
    YO_NG,
    ID_ID,
    EN_WS,
    DE_CH,
    PT_PT,
    VAL_ES,
    NN_NO,
    LB_LU,
    DE_DE,
    ES_CL,
    PT_BR,
    RU_RU,
    SO_SO,
    HU_HU,
    SR_SP,
    LV_LV,
    OC_FR,
    MK_MK,
    SL_SI,
    NO_NO,
    ZH_TW,
    BR_FR,
    TA_IN,
    AF_ZA,
    FY_NL,
    ET_EE,
    MS_MY,
    AZ_AZ,
    ES_UY,
    AST_ES,
    FA_IR,
    SK_SK,
    KO_KR,
    MT_MT,
    CA_ES,
    ES_VE,
    CY_GB,
    EN_NZ,
    JA_JP,
    RO_RO,
    LOL_US,
    ZH_CN;

    /**
     * Обозначает, является ли язык скачиваемым.
     * По умолчанию все языки скачиваемые.
     *
     * @return является ли язык скачиваемым
     */
    public boolean isExternal() {
        return true;
    }
}

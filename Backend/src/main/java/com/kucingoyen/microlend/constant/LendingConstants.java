package com.kucingoyen.microlend.constant;

/**
 * Constants for the Lending system including DAML template IDs and
 * configuration.
 */
public class LendingConstants {

    // DAML Template IDs (without package ID, will be added by DamlLedgerService)
    public static final String TEMPLATE_LENDING_SERVICE = "MicroLend.Finance.Lending.LendingService:LendingService";
    public static final String TEMPLATE_LOAN_REQUEST = "MicroLend.Finance.Lending.LoanRequest:LoanRequest";
    public static final String TEMPLATE_ACTIVE_LOAN = "MicroLend.Finance.Lending.ActiveLoan:ActiveLoan";
    public static final String TEMPLATE_USER_PROFILE = "MicroLend.Finance.Lending.UserProfile:UserProfile";
    public static final String TEMPLATE_USER_PROFILE_FACTORY = "MicroLend.Finance.Lending.UserProfile:UserProfileFactory";
    public static final String TEMPLATE_TIME_ORACLE = "MicroLend.Finance.Lending.TimeOracle:TimeOracle";

    // System Config Keys
    public static final String CONFIG_LENDING_SERVICE_CID = "lending.service.contract.id";
    public static final String CONFIG_TIME_ORACLE_CID = "lending.timeoracle.contract.id";
    public static final String CONFIG_USER_PROFILE_FACTORY_CID = "lending.userprofilefactory.contract.id";

    // Default Configuration Values
    public static final String DEFAULT_BASE_COLLATERAL_RATE = "1.10";
    public static final String DEFAULT_INTEREST_RATE = "0.05";
    public static final int DEFAULT_LOAN_DURATION_DAYS = 30;

    // User Level Collateral Bonus (percentage added to base rate)
    public static final String LEVEL1_BONUS = "0.05"; // 5%
    public static final String LEVEL2_BONUS = "0.04"; // 4%
    public static final String LEVEL3_BONUS = "0.03"; // 3%
    public static final String LEVEL4_BONUS = "0.02"; // 2%
    public static final String LEVEL5_BONUS = "0.01"; // 1%

    // User Level Names
    public static final String LEVEL_1 = "Level1";
    public static final String LEVEL_2 = "Level2";
    public static final String LEVEL_3 = "Level3";
    public static final String LEVEL_4 = "Level4";
    public static final String LEVEL_5 = "Level5";

    private LendingConstants() {
        // Prevent instantiation
    }
}

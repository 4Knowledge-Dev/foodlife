package com.forknowledge.feature.onboarding;

@kotlin.Metadata(mv = {2, 1, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u0017\b\u0007\u0018\u00002\u00020\u0001B\t\b\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010g\u001a\u00020hJ\u0006\u0010i\u001a\u00020hJ\u000e\u0010j\u001a\u00020h2\u0006\u0010k\u001a\u00020\u0011J\u000e\u0010l\u001a\u00020h2\u0006\u0010m\u001a\u00020\u0019J\u000e\u0010n\u001a\u00020h2\u0006\u0010o\u001a\u00020 J\u000e\u0010p\u001a\u00020h2\u0006\u0010q\u001a\u00020 J\u000e\u0010r\u001a\u00020h2\u0006\u0010q\u001a\u00020 J\u000e\u0010s\u001a\u00020h2\u0006\u0010t\u001a\u000207J\u000e\u0010u\u001a\u00020h2\u0006\u0010v\u001a\u00020>J\u000e\u0010w\u001a\u00020h2\u0006\u0010x\u001a\u00020IJ\u000e\u0010y\u001a\u00020h2\u0006\u0010z\u001a\u00020QJ\u000e\u0010{\u001a\u00020h2\u0006\u0010|\u001a\u00020XJ\u000e\u0010}\u001a\u00020h2\u0006\u0010~\u001a\u00020XR+\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u00058F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u000b\u0010\f\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0011\u0010\r\u001a\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R/\u0010\u0012\u001a\u0004\u0018\u00010\u00112\b\u0010\u0004\u001a\u0004\u0018\u00010\u00118F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R/\u0010\u001a\u001a\u0004\u0018\u00010\u00192\b\u0010\u0004\u001a\u0004\u0018\u00010\u00198F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u001f\u0010\u0018\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR+\u0010!\u001a\u00020 2\u0006\u0010\u0004\u001a\u00020 8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b&\u0010\'\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R+\u0010(\u001a\u00020 2\u0006\u0010\u0004\u001a\u00020 8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b+\u0010\'\u001a\u0004\b)\u0010#\"\u0004\b*\u0010%R+\u0010,\u001a\u00020 2\u0006\u0010\u0004\u001a\u00020 8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b/\u0010\'\u001a\u0004\b-\u0010#\"\u0004\b.\u0010%R/\u00101\u001a\u0004\u0018\u0001002\b\u0010\u0004\u001a\u0004\u0018\u0001008F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b6\u0010\u0018\u001a\u0004\b2\u00103\"\u0004\b4\u00105R/\u00108\u001a\u0004\u0018\u0001072\b\u0010\u0004\u001a\u0004\u0018\u0001078F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b=\u0010\u0018\u001a\u0004\b9\u0010:\"\u0004\b;\u0010<R/\u0010?\u001a\u0004\u0018\u00010>2\b\u0010\u0004\u001a\u0004\u0018\u00010>8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\bD\u0010\u0018\u001a\u0004\b@\u0010A\"\u0004\bB\u0010CR+\u0010E\u001a\u00020 2\u0006\u0010\u0004\u001a\u00020 8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\bH\u0010\'\u001a\u0004\bF\u0010#\"\u0004\bG\u0010%R/\u0010J\u001a\u0004\u0018\u00010I2\b\u0010\u0004\u001a\u0004\u0018\u00010I8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\bO\u0010\u0018\u001a\u0004\bK\u0010L\"\u0004\bM\u0010NR7\u0010R\u001a\b\u0012\u0004\u0012\u00020Q0P2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020Q0P8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\bW\u0010\u0018\u001a\u0004\bS\u0010T\"\u0004\bU\u0010VR+\u0010Y\u001a\u00020X2\u0006\u0010\u0004\u001a\u00020X8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b]\u0010\u0018\u001a\u0004\bY\u0010Z\"\u0004\b[\u0010\\R+\u0010^\u001a\u00020X2\u0006\u0010\u0004\u001a\u00020X8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b`\u0010\u0018\u001a\u0004\b^\u0010Z\"\u0004\b_\u0010\\R+\u0010a\u001a\u00020X2\u0006\u0010\u0004\u001a\u00020X8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\bc\u0010\u0018\u001a\u0004\ba\u0010Z\"\u0004\bb\u0010\\R\u001a\u0010d\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\be\u0010\b\"\u0004\bf\u0010\n\u00a8\u0006\u007f"}, d2 = {"Lcom/forknowledge/feature/onboarding/OnboardingViewModel;", "Landroidx/lifecycle/ViewModel;", "<init>", "()V", "<set-?>", "", "progress", "getProgress", "()I", "setProgress", "(I)V", "progress$delegate", "Landroidx/compose/runtime/MutableIntState;", "question", "Lcom/forknowledge/core/common/healthtype/SurveyQuestionType;", "getQuestion", "()Lcom/forknowledge/core/common/healthtype/SurveyQuestionType;", "Lcom/forknowledge/core/common/healthtype/Gender;", "gender", "getGender", "()Lcom/forknowledge/core/common/healthtype/Gender;", "setGender", "(Lcom/forknowledge/core/common/healthtype/Gender;)V", "gender$delegate", "Landroidx/compose/runtime/MutableState;", "", "birthday", "getBirthday", "()Ljava/lang/Long;", "setBirthday", "(Ljava/lang/Long;)V", "birthday$delegate", "", "height", "getHeight", "()D", "setHeight", "(D)V", "height$delegate", "Landroidx/compose/runtime/MutableDoubleState;", "currentWeight", "getCurrentWeight", "setCurrentWeight", "currentWeight$delegate", "targetWeight", "getTargetWeight", "setTargetWeight", "targetWeight$delegate", "Lcom/forknowledge/core/common/healthtype/TargetWeightError;", "targetWeightError", "getTargetWeightError", "()Lcom/forknowledge/core/common/healthtype/TargetWeightError;", "setTargetWeightError", "(Lcom/forknowledge/core/common/healthtype/TargetWeightError;)V", "targetWeightError$delegate", "Lcom/forknowledge/core/common/healthtype/Goal;", "goal", "getGoal", "()Lcom/forknowledge/core/common/healthtype/Goal;", "setGoal", "(Lcom/forknowledge/core/common/healthtype/Goal;)V", "goal$delegate", "Lcom/forknowledge/core/common/healthtype/ActivityLevel;", "activityLevel", "getActivityLevel", "()Lcom/forknowledge/core/common/healthtype/ActivityLevel;", "setActivityLevel", "(Lcom/forknowledge/core/common/healthtype/ActivityLevel;)V", "activityLevel$delegate", "weightPerWeek", "getWeightPerWeek", "setWeightPerWeek", "weightPerWeek$delegate", "Lcom/forknowledge/core/common/healthtype/Diet;", "diet", "getDiet", "()Lcom/forknowledge/core/common/healthtype/Diet;", "setDiet", "(Lcom/forknowledge/core/common/healthtype/Diet;)V", "diet$delegate", "", "", "excludes", "getExcludes", "()Ljava/util/List;", "setExcludes", "(Ljava/util/List;)V", "excludes$delegate", "", "isCmUnit", "()Z", "setCmUnit", "(Z)V", "isCmUnit$delegate", "isKgUnit", "setKgUnit", "isKgUnit$delegate", "isNextEnable", "setNextEnable", "isNextEnable$delegate", "targetCalories", "getTargetCalories", "setTargetCalories", "onPreviousClicked", "", "onNextClicked", "onGenderSelected", "userGender", "onBirthdayChanged", "userBirthday", "onHeightChanged", "userHeight", "onCurrentWeightChanged", "weight", "onTargetWeightChanged", "onGoalSelected", "userGoal", "onActivityLevelSelected", "userActivityLevel", "onDietSelected", "userDiet", "onIngredientSelected", "exclude", "onHeightUnitChanged", "lengthUnit", "onWeightUnitChanged", "weightUnit", "onboarding_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class OnboardingViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableIntState progress$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState gender$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState birthday$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableDoubleState height$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableDoubleState currentWeight$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableDoubleState targetWeight$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState targetWeightError$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState goal$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState activityLevel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableDoubleState weightPerWeek$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState diet$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState excludes$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState isCmUnit$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState isKgUnit$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState isNextEnable$delegate = null;
    private int targetCalories = 0;
    
    @javax.inject.Inject()
    public OnboardingViewModel() {
        super();
    }
    
    public final int getProgress() {
        return 0;
    }
    
    private final void setProgress(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.forknowledge.core.common.healthtype.SurveyQuestionType getQuestion() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.forknowledge.core.common.healthtype.Gender getGender() {
        return null;
    }
    
    private final void setGender(com.forknowledge.core.common.healthtype.Gender p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getBirthday() {
        return null;
    }
    
    private final void setBirthday(java.lang.Long p0) {
    }
    
    public final double getHeight() {
        return 0.0;
    }
    
    private final void setHeight(double p0) {
    }
    
    public final double getCurrentWeight() {
        return 0.0;
    }
    
    private final void setCurrentWeight(double p0) {
    }
    
    public final double getTargetWeight() {
        return 0.0;
    }
    
    private final void setTargetWeight(double p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.forknowledge.core.common.healthtype.TargetWeightError getTargetWeightError() {
        return null;
    }
    
    public final void setTargetWeightError(@org.jetbrains.annotations.Nullable()
    com.forknowledge.core.common.healthtype.TargetWeightError p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.forknowledge.core.common.healthtype.Goal getGoal() {
        return null;
    }
    
    private final void setGoal(com.forknowledge.core.common.healthtype.Goal p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.forknowledge.core.common.healthtype.ActivityLevel getActivityLevel() {
        return null;
    }
    
    private final void setActivityLevel(com.forknowledge.core.common.healthtype.ActivityLevel p0) {
    }
    
    public final double getWeightPerWeek() {
        return 0.0;
    }
    
    private final void setWeightPerWeek(double p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.forknowledge.core.common.healthtype.Diet getDiet() {
        return null;
    }
    
    private final void setDiet(com.forknowledge.core.common.healthtype.Diet p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getExcludes() {
        return null;
    }
    
    private final void setExcludes(java.util.List<java.lang.String> p0) {
    }
    
    public final boolean isCmUnit() {
        return false;
    }
    
    private final void setCmUnit(boolean p0) {
    }
    
    public final boolean isKgUnit() {
        return false;
    }
    
    private final void setKgUnit(boolean p0) {
    }
    
    public final boolean isNextEnable() {
        return false;
    }
    
    private final void setNextEnable(boolean p0) {
    }
    
    public final int getTargetCalories() {
        return 0;
    }
    
    public final void setTargetCalories(int p0) {
    }
    
    public final void onPreviousClicked() {
    }
    
    public final void onNextClicked() {
    }
    
    public final void onGenderSelected(@org.jetbrains.annotations.NotNull()
    com.forknowledge.core.common.healthtype.Gender userGender) {
    }
    
    public final void onBirthdayChanged(long userBirthday) {
    }
    
    public final void onHeightChanged(double userHeight) {
    }
    
    public final void onCurrentWeightChanged(double weight) {
    }
    
    public final void onTargetWeightChanged(double weight) {
    }
    
    public final void onGoalSelected(@org.jetbrains.annotations.NotNull()
    com.forknowledge.core.common.healthtype.Goal userGoal) {
    }
    
    public final void onActivityLevelSelected(@org.jetbrains.annotations.NotNull()
    com.forknowledge.core.common.healthtype.ActivityLevel userActivityLevel) {
    }
    
    public final void onDietSelected(@org.jetbrains.annotations.NotNull()
    com.forknowledge.core.common.healthtype.Diet userDiet) {
    }
    
    public final void onIngredientSelected(@org.jetbrains.annotations.NotNull()
    java.lang.String exclude) {
    }
    
    public final void onHeightUnitChanged(boolean lengthUnit) {
    }
    
    public final void onWeightUnitChanged(boolean weightUnit) {
    }
}
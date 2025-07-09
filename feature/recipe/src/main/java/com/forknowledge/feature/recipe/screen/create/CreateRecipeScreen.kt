package com.forknowledge.feature.recipe.screen.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.forknowledge.core.common.extension.toFormattedNumber
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.Grey8A949F
import com.forknowledge.core.ui.theme.GreyB7BDC4
import com.forknowledge.core.ui.theme.GreyC5C6C7
import com.forknowledge.core.ui.theme.GreyFAFAFA
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppBasicTextField
import com.forknowledge.core.ui.theme.component.AppButton
import com.forknowledge.core.ui.theme.component.AppSnackBar
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.LoadingIndicatorOverlay
import com.forknowledge.core.ui.theme.state.SnackBarState
import com.forknowledge.feature.model.Ingredient
import com.forknowledge.feature.model.Recipe
import com.forknowledge.feature.model.Step
import com.forknowledge.feature.recipe.R
import com.forknowledge.feature.recipe.screen.AddMethodBottomSheet
import com.forknowledge.feature.recipe.screen.AddSourceBottomSheet
import com.forknowledge.feature.recipe.type.RecipeBottomSheetType
import kotlinx.coroutines.launch

const val ATTRIBUTE_TITLE_MAX_LENGTH = 100
const val ATTRIBUTE_DESCRIPTION_MAX_LENGTH = 500
const val ATTRIBUTE_SERVINGS_MAX_LENGTH = 2
const val ATTRIBUTE_TIME_MAX_LENGTH = 3

@Composable
fun CreateRecipeScreen(
    viewModel: CreateRecipeViewModel = hiltViewModel(),
    onNavigateToRecipeDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val recipe by viewModel.recipe.collectAsState()
    val method = viewModel.method
    val shouldShowLoading = viewModel.shouldShowLoading
    val shouldShowSaveRecipeError = viewModel.shouldShowSaveRecipeError
    val onNavigateToRecipeDetail = viewModel.onNavigateToRecipeDetail

    var isTitleValid by remember { mutableStateOf(true) }
    var isServingsValid by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }

    val snackBarMessage = stringResource(R.string.create_recipe_save_recipe_error)
    LaunchedEffect(shouldShowSaveRecipeError) {
        if (shouldShowSaveRecipeError) {
            snackbarHostState.showSnackbar(
                message = snackBarMessage,
                duration = SnackbarDuration.Short
            )
        }
    }

    if (onNavigateToRecipeDetail) {
        onNavigateToRecipeDetail(recipe.recipeId)
    }

    Scaffold(
        topBar = { CreateRecipeTopBar(onNavigateBack) },
        bottomBar = {
            CreateRecipeBottomBar {
                isTitleValid = recipe.recipeName.isNotEmpty()
                isServingsValid = recipe.servings > 0
                if (isServingsValid && isTitleValid) {
                    viewModel.saveRecipe()
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    AppSnackBar(
                        message = snackbarData.visuals.message,
                        state = SnackBarState.FAILURE
                    )
                }
            )
        }
    ) { innerPadding ->
        if (shouldShowLoading) {
            LoadingIndicatorOverlay()
        }

        CreateRecipeContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(White)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp),
            recipe = recipe,
            method = method,
            isTitleValid = isTitleValid,
            isServingsValid = isServingsValid,
            onTitleChanged = { viewModel.updateRecipeName(it) },
            onDescriptionChanged = { viewModel.updateSummary(it) },
            onRecipeSourceChanged = { sourceName, sourceUrl ->
                viewModel.updateRecipeSource(sourceName, sourceUrl)
            },
            onServingsChanged = { viewModel.updateServings(it) },
            onPrepTimeChanged = { viewModel.updatePrepTime(it) },
            onCookTimeChanged = { viewModel.updateCookTime(it) },
            onMethodChanged = { viewModel.updateMethod(it) },
            onAddIngredient = { viewModel.addIngredient(it) },
            onRemoveIngredient = { viewModel.removeIngredient(it) },
            onUpdateInstructions = { viewModel.updateInstructions(it) }
        )
    }
}

@Composable
fun CreateRecipeTopBar(
    onNavigateBack: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        val (backButton, title) = createRefs()

        IconButton(
            modifier = Modifier.constrainAs(backButton) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            onClick = onNavigateBack
        ) {
            Icon(
                painter = painterResource(drawable.ic_back),
                tint = Grey8A949F,
                contentDescription = null
            )
        }

        AppText(
            modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            text = stringResource(R.string.create_recipe_top_bar_title),
            textStyle = Typography.labelLarge
        )
    }
}

@Composable
fun CreateRecipeBottomBar(
    onCreateRecipe: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(GreyFAFAFA)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            buttonText = stringResource(R.string.create_recipe_button_create),
            buttonColor = Green91C747,
            textStyle = Typography.labelMedium,
            onClicked = { onCreateRecipe() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipeContent(
    modifier: Modifier = Modifier,
    recipe: Recipe,
    method: String,
    isTitleValid: Boolean,
    isServingsValid: Boolean,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onRecipeSourceChanged: (sourceName: String, sourceUrl: String) -> Unit,
    onServingsChanged: (Int) -> Unit,
    onPrepTimeChanged: (Int) -> Unit,
    onCookTimeChanged: (Int) -> Unit,
    onMethodChanged: (String) -> Unit,
    onAddIngredient: (String) -> Unit,
    onRemoveIngredient: (Ingredient) -> Unit,
    onUpdateInstructions: (Step) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var sheetType by remember { mutableStateOf(RecipeBottomSheetType.RECIPE_SOURCE) }
    var ingredientInput by remember { mutableStateOf("") }
    var instructionInput by remember { mutableStateOf("") }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false },
        ) {
            when (sheetType) {
                RecipeBottomSheetType.RECIPE_SOURCE -> {
                    AddSourceBottomSheet(
                        sourceName = recipe.sourceName,
                        url = recipe.sourceUrl,
                        onSave = { sourceName, url ->
                            showBottomSheet = false
                            onRecipeSourceChanged(sourceName, url)
                        }
                    )
                }

                RecipeBottomSheetType.RECIPE_METHOD -> {
                    AddMethodBottomSheet(
                        method = method,
                        onSave = { method ->
                            showBottomSheet = false
                            onMethodChanged(method)
                        }
                    )
                }
            }

        }
    }

    Column(
        modifier = modifier.imePadding()
    ) {
        AppText(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.create_recipe_title_label),
            textStyle = Typography.labelLarge
        )

        AppBasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            value = recipe.recipeName,
            placeholder = stringResource(R.string.create_recipe_title_text_field_placeholder),
            textStyle = Typography.bodySmall,
            singleLine = true,
            onValueChanged = { title ->
                if (title.length <= ATTRIBUTE_TITLE_MAX_LENGTH) {
                    onTitleChanged(title)
                }
            }
        )

        if (!isTitleValid) {
            AppText(
                modifier = Modifier
                    .padding(top = 8.dp, end = 16.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.create_recipe_title_label_empty_error),
                textStyle = Typography.bodySmall,
                color = RedFF4950,
                textAlign = TextAlign.End
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = GreyC5C6C7,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        sheetType = RecipeBottomSheetType.RECIPE_SOURCE
                        showBottomSheet = true
                    },
                contentAlignment = Alignment.Center
            ) {
                AppText(
                    modifier = Modifier.padding(4.dp),
                    text = stringResource(R.string.create_recipe_add_source_button),
                    textStyle = Typography.labelSmall
                )
            }

            AppText(
                modifier = Modifier.padding(start = 24.dp),
                text = recipe.sourceName.ifEmpty { recipe.sourceUrl },
                textStyle = Typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        AppText(
            modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp),
            text = stringResource(R.string.create_recipe_description_label),
            textStyle = Typography.labelLarge
        )

        AppBasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            value = recipe.summary,
            placeholder = stringResource(R.string.create_recipe_description_text_field_placeholder),
            textStyle = Typography.bodySmall,
            minLines = 3,
            onValueChanged = { description ->
                if (description.length <= ATTRIBUTE_DESCRIPTION_MAX_LENGTH) {
                    onDescriptionChanged(description)
                }
            }
        )

        RecipeSetInformation(
            modifier = Modifier.padding(top = 24.dp),
            title = stringResource(R.string.create_recipe_bottom_sheet_servings_title),
            value = recipe.servings.toString(),
            description = stringResource(R.string.create_recipe_set_servings_description),
            focusedTextFieldColor = if (isServingsValid) Color.Black else RedFF4950,
            unfocusedTextFieldColor = if (isServingsValid) Grey8A949F else RedFF4950,
            onValueChanged = { servings ->
                if (servings.length <= ATTRIBUTE_SERVINGS_MAX_LENGTH) {
                    onServingsChanged(if (servings.isEmpty()) 0 else servings.toInt())
                }
            }
        )

        RecipeSetInformation(
            modifier = Modifier.padding(top = 24.dp),
            title = stringResource(R.string.create_recipe_set_prep_time_title),
            value = recipe.preparationMinutes.toString(),
            description = stringResource(R.string.create_recipe_set_prep_time_description),
            onValueChanged = { time ->
                if (time.length <= ATTRIBUTE_TIME_MAX_LENGTH) {
                    onPrepTimeChanged(if (time.isEmpty()) 0 else time.toInt())
                }
            }
        )

        RecipeSetInformation(
            modifier = Modifier.padding(top = 24.dp),
            title = stringResource(R.string.create_recipe_set_cook_time_title),
            value = recipe.cookingMinutes.toString(),
            description = stringResource(R.string.create_recipe_set_cook_time_description),
            onValueChanged = { time ->
                if (time.length <= ATTRIBUTE_TIME_MAX_LENGTH) {
                    onCookTimeChanged(if (time.isEmpty()) 0 else time.toInt())
                }
            }
        )

        Row(
            modifier = Modifier
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AppText(
                text = stringResource(R.string.create_recipe_method_label),
                textStyle = Typography.labelLarge
            )

            Box(
                modifier = Modifier
                    .background(
                        color = White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = GreyC5C6C7,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        sheetType = RecipeBottomSheetType.RECIPE_METHOD
                        showBottomSheet = true
                    },
                contentAlignment = Alignment.Center
            ) {
                AppText(
                    modifier = Modifier.padding(4.dp),
                    text = stringResource(R.string.create_recipe_bottom_sheet_add_method_title),
                    textStyle = Typography.labelSmall
                )
            }
        }

        // Ingredients
        AppText(
            modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
            text = stringResource(R.string.recipe_ingredients_tab_title),
            textStyle = Typography.labelMedium
        )

        AppText(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
            text = stringResource(R.string.create_recipe_item_instruction),
            textStyle = Typography.bodySmall,
            color = Grey8A949F
        )

        recipe.ingredients.forEachIndexed { index, ingredient ->
            val ingredientAmount = buildAnnotatedString {
                if (ingredient.originalMeasures.amount > 0) {
                    val formattedAmount = ingredient.originalMeasures.amount.toFormattedNumber()
                    withStyle(
                        style = Typography.bodyMedium.toSpanStyle().copy(
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("$formattedAmount ${ingredient.originalMeasures.unit} ")
                    }
                }
                withStyle(
                    style = Typography.bodyMedium.toSpanStyle()
                ) {
                    append(ingredient.ingredientName)
                }
            }

            SwipeToDeleteItem(
                modifier = Modifier.padding(top = 16.dp),
                onRemove = { onRemoveIngredient(ingredient) },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier.size(40.dp),
                        model = ingredient.imageUrl,
                        error = painterResource(R.drawable.img_ingredient_error),
                        contentDescription = null,
                    )

                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = ingredientAmount
                    )
                }
            }
        }

        AppBasicTextField(
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                    }
                }
                .padding(top = 16.dp)
                .fillMaxWidth()
                .padding(top = 21.dp, start = 16.dp, end = 16.dp),
            value = ingredientInput,
            placeholder = stringResource(R.string.create_recipe_ingredient_text_field_placeholder),
            textStyle = Typography.bodySmall,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (ingredientInput.isNotEmpty()) {
                        onAddIngredient(ingredientInput)
                        ingredientInput = ""
                        keyboardController?.hide()
                    }
                }
            ),
            onValueChanged = { ingredientInput = it }
        )

        // Instructions
        AppText(
            modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp),
            text = stringResource(R.string.recipe_instructions_tab_title),
            textStyle = Typography.labelMedium
        )

        AppText(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
            text = stringResource(R.string.create_recipe_item_instruction),
            textStyle = Typography.bodySmall,
            color = Grey8A949F
        )

        recipe.steps.forEachIndexed { index, step ->
            SwipeToDeleteItem(
                modifier = Modifier.padding(top = 16.dp),
                onRemove = { onUpdateInstructions(step) }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    AppText(
                        text = stringResource(
                            R.string.recipe_instruction_step_number,
                            index + 1
                        ),
                        textStyle = Typography.labelMedium,
                        color = Grey8A949F
                    )

                    AppText(
                        text = step.description,
                        textStyle = Typography.bodyMedium
                    )
                }
            }
        }

        AppBasicTextField(
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                    }
                }
                .fillMaxWidth()
                .padding(top = 21.dp, start = 16.dp, end = 16.dp),
            value = instructionInput,
            placeholder = stringResource(R.string.create_recipe_ingredient_text_field_placeholder),
            textStyle = Typography.bodySmall,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (instructionInput.isNotEmpty()) {
                        onUpdateInstructions(
                            Step(
                                stepNumber = recipe.steps.size + 1,
                                description = instructionInput,
                                equipments = emptyList(),
                                ingredients = emptyList()
                            )
                        )
                        instructionInput = ""
                        keyboardController?.hide()
                    }
                }
            ),
            onValueChanged = { instructionInput = it }
        )
    }
}

@Composable
fun RecipeSetInformation(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    description: String,
    unfocusedTextFieldColor: Color = GreyB7BDC4,
    focusedTextFieldColor: Color = Color.Black,
    onValueChanged: (String) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val (textTitle, textField, textDescription) = createRefs()

        AppText(
            modifier = Modifier.constrainAs(textTitle) {
                start.linkTo(parent.start)
                top.linkTo(textField.top)
                bottom.linkTo(textField.bottom)
            },
            text = title,
            textStyle = Typography.labelLarge
        )

        AppBasicTextField(
            modifier = Modifier
                .width(60.dp)
                .constrainAs(textField) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
            textStyle = Typography.bodyMedium.copy(textAlign = TextAlign.Center),
            unfocusedTextFieldColor = unfocusedTextFieldColor,
            focusedTextFieldColor = focusedTextFieldColor,
            value = value.ifEmpty { "" },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChanged = { value ->
                onValueChanged(value)
            }
        )

        AppText(
            modifier = Modifier.constrainAs(textDescription) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(textTitle.bottom, margin = 12.dp)
                horizontalBias = 0f
            },
            text = description,
            textStyle = Typography.bodySmall,
            color = Grey8A949F
        )


    }
}

@Composable
fun SwipeToDeleteItem(
    modifier: Modifier = Modifier,
    onRemove: (Int) -> Unit,
    content: @Composable (RowScope.() -> Unit)
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onRemove(0)
            }
            false
        }
    )

    SwipeToDismissBox(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        state = swipeToDismissBoxState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                        .wrapContentSize(Alignment.CenterEnd)
                        .padding(8.dp),
                    imageVector = Icons.Default.Delete,
                    tint = White,
                    contentDescription = null
                )
            } else { /* Do nothing */
            }
        },
        content = content
    )
}

@Preview
@Composable
fun CreateRecipeTopBarPreview() {
    CreateRecipeTopBar(
        onNavigateBack = {}
    )
}

@Preview
@Composable
fun CreateRecipeBottomBarPreview() {
    CreateRecipeBottomBar(
        onCreateRecipe = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CreateRecipeContentPreview() {
    CreateRecipeContent(
        recipe = Recipe(),
        method = "",
        isTitleValid = false,
        isServingsValid = false,
        onTitleChanged = {},
        onDescriptionChanged = {},
        onRecipeSourceChanged = { _, _ -> },
        onServingsChanged = {},
        onPrepTimeChanged = {},
        onCookTimeChanged = {},
        onMethodChanged = {},
        onAddIngredient = {},
        onRemoveIngredient = {},
        onUpdateInstructions = {}
    )
}
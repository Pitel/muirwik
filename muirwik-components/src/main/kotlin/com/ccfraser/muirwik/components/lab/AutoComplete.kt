package com.ccfraser.muirwik.components.lab

import com.ccfraser.muirwik.components.*
import kotlinext.js.Object
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import react.*
import styled.StyledHandler

@JsModule("@material-ui/lab/Autocomplete")
private external val autoCompleteModule: dynamic

@Suppress("UnsafeCastFromDynamic")
private val autoCompleteComponent = autoCompleteModule.default

@Suppress("EnumEntryName")
enum class MAutoCompleteBlurOnSelect {
    mouse, touch, yes, no;

    override fun toString(): String {
        return when(this) {
            mouse -> "mouse"
            touch -> "touch"
            yes -> "true"
            no -> "false"
        }
    }
}

@Suppress("EnumEntryName")
enum class MAutoCompleteForcePopupIcon {
    auto, yes, no;

    override fun toString(): String {
        return when(this) {
            auto -> "auto"
            yes -> "true"
            no -> "false"
        }
    }
}

enum class MAutoCompleteOnCloseReason {
    toggleInput, escape, selectOption, blur
}

/**
 * Because the Material UI AutoComplete uses union types for its onChange and defaultValue (a T or an array of T), we
 * create two separate components, one that is for the singular use case, and one where we want to use mutli-values.
 */
external interface MAutoCompleteBaseProps<T> : StyledPropsWithCommonAttributes {
    var autoComplete: Boolean
    var autoHighlight: Boolean
    var autoSelect: Boolean
    var blurOnSelect: MAutoCompleteBlurOnSelect
    @JsName("ChipProps")
    var chipProps: RProps
    var clearOnBlur: Boolean
    var clearOnEscape: Boolean
    var clearText: String
    var closeIcon: Node
    var closeText: String
    var debug: Boolean
    var disableClearable: Boolean
    var disableCloseOnSelect: Boolean
    var disabled: Boolean
    var disabledItemsFocusable: Boolean
    var disableListWrap: Boolean
    var disablePortal: Boolean
    var filterOptions: (options: Array<T>, state: Any) -> Unit
    var filterSelectedOptions: Boolean
    var forcePopupIcon: MAutoCompleteForcePopupIcon
    var freeSolo: Boolean
    var fullWidth: Boolean
    var getLimitTagsText: (more: Int) -> ReactElement
    var getOptionDisabled: (option: T) -> Boolean
    var getOptionLabel: (option: T?) -> String
    var getOptionSelected: (option: T?, value: T) -> Boolean
    var groupBy: (option: T) -> String
    var handleHomeEndKeys: Boolean
    var includeInputInList: Boolean
    var inputValue: String
    var limitTags: Int
    @JsName("ListboxComponent")
    var listboxComponent: ElementType
    @JsName("ListboxProps")
    var listboxProps: RProps
    var loading: Boolean
    var loadingText: Node
    var multiple: Boolean
    var noOptionsText: Node
//  wrapped onClose below
//  var onClose: (event: Event, reason: MAutoCompleteOnCloseReason) -> Unit
    var onHighlightChange: (event: Event, option: T?, reason: String) -> Unit
    var onInputChange: (event: Event, value: String, reason: String) -> Unit
    var onOpen: (event: Event) -> Unit
    var openOnFocus: Boolean
    var openText: String
    var options: Array<T>
    @JsName("PaperComponent")
    var paperComponent: ReactElement
    @JsName("PopperComponent")
    var popperComponent: ReactElement
    var popupIcon: Node
    var renderGroup: (option: T) -> ReactElement
    var renderInput: (params: RProps) -> ReactElement
    var renderOption: (option: T, state: Object) -> ReactElement
    var renderTags: (value: Array<T>, getTagProps: () -> RProps) -> ReactElement
    var selectOnFocus: Boolean
}
var <T> MAutoCompleteBaseProps<T>.size by EnumPropToString(MRatingSize.values())
var <T> MAutoCompleteBaseProps<T>.onClose by OnClosePropWithReasonDelegate(MAutoCompleteOnCloseReason.values())
// Have not wrapped the other events with reasons yet as they have more params than onClose which is already implemented...

/**
 * This is the props for an auto complete that only has one value (multiple = false)
 */
external interface MAutoCompleteProps<T> : MAutoCompleteBaseProps<T> {
    var defaultValue: T?
    var onChange: (event: Event, value: T?, reason: String) -> Unit
    var value: T?
}

/**
* This is the props for an auto complete that can have multiple values (multiple = true)
*/
external interface MAutoCompletePropsMultiValue<T> : MAutoCompleteBaseProps<T> {
    var defaultValue: Array<T>?
    var onChange: (event: Event, value: Array<T>, reason: String) -> Unit
    var value: Array<T>?
}

/**
 * This version of AutoComplete allows only single values. In Material UI, the AutoComplete control handles both
 * singular and multi-value controls via union types which we can't simulate, so we have two separate components instead.
 */
fun <T> RBuilder.mAutoComplete(
    options: Array<T>,
    renderInput: (params: RProps) -> ReactElement,
    value: T? = null,

    addAsChild: Boolean = true,
    className: String? = null,
    handler: StyledHandler<MAutoCompleteProps<T>>? = null
): ReactElement {
    val myComponent: RComponent<MAutoCompleteProps<T>, RState> = autoCompleteComponent

    return createStyled(myComponent, addAsChild) {
        attrs.options = options
        attrs.renderInput = renderInput
        value?.let { attrs.value = it }
        setStyledPropsAndRunHandler(className, handler)
    }
}

/**
 * This version of AutoComplete allows multiple values. In Material UI, the AutoComplete control handles both
 * singular and multi-value controls via union types which we can't simulate, so we have two separate components instead.
 */
fun <T> RBuilder.mAutoCompleteMultiValue(
    options: Array<T>,
    renderInput: (params: RProps) -> ReactElement,
    value: Array<T>? = null,

    addAsChild: Boolean = true,
    className: String? = null,
    handler: StyledHandler<MAutoCompletePropsMultiValue<T>>? = null
): ReactElement {
    val myComponent: RComponent<MAutoCompletePropsMultiValue<T>, RState> = autoCompleteComponent

    return createStyled(myComponent, addAsChild) {
        attrs.options = options
        attrs.renderInput = renderInput
        attrs.multiple = true
        value?.let { attrs.value = it }
        setStyledPropsAndRunHandler(className, handler)
    }
}

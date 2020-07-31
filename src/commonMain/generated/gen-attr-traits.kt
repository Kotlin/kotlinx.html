package kotlinx.html

/*******************************************************************************
DO NOT EDIT
This file was generated by module generate
 *******************************************************************************/

interface CommonAttributeGroupFacade<E> : Tag<E>

var <E> CommonAttributeGroupFacade<E>.enableTheming: Boolean
    get() = attributeBooleanBoolean.get(this, "EnableTheming")
    set(newValue) {
        attributeBooleanBoolean.set(this, "EnableTheming", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.enableViewState: Boolean
    get() = attributeBooleanBoolean.get(this, "EnableViewState")
    set(newValue) {
        attributeBooleanBoolean.set(this, "EnableViewState", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.skinID: String
    get() = attributeStringString.get(this, "SkinID")
    set(newValue) {
        attributeStringString.set(this, "SkinID", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.visible: Boolean
    get() = attributeBooleanBoolean.get(this, "Visible")
    set(newValue) {
        attributeBooleanBoolean.set(this, "Visible", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.accessKey: String
    get() = attributeStringString.get(this, "accesskey")
    set(newValue) {
        attributeStringString.set(this, "accesskey", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.classes: Set<String>
    get() = attributeSetStringStringSet.get(this, "class")
    set(newValue) {
        attributeSetStringStringSet.set(this, "class", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.contentEditable: Boolean
    get() = attributeBooleanBoolean.get(this, "contenteditable")
    set(newValue) {
        attributeBooleanBoolean.set(this, "contenteditable", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.contextMenu: String
    get() = attributeStringString.get(this, "contextmenu")
    set(newValue) {
        attributeStringString.set(this, "contextmenu", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.dataFolderName: String
    get() = attributeStringString.get(this, "data-FolderName")
    set(newValue) {
        attributeStringString.set(this, "data-FolderName", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.dataMsgId: String
    get() = attributeStringString.get(this, "data-MsgId")
    set(newValue) {
        attributeStringString.set(this, "data-MsgId", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.dir: Dir
    get() = attributeDirEnumDirValues.get(this, "dir")
    set(newValue) {
        attributeDirEnumDirValues.set(this, "dir", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.draggable: Draggable
    get() = attributeDraggableEnumDraggableValues.get(this, "draggable")
    set(newValue) {
        attributeDraggableEnumDraggableValues.set(this, "draggable", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.hidden: Boolean
    get() = attributeBooleanTicker.get(this, "hidden")
    set(newValue) {
        attributeBooleanTicker.set(this, "hidden", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.id: String
    get() = attributeStringString.get(this, "id")
    set(newValue) {
        attributeStringString.set(this, "id", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.itemProp: String
    get() = attributeStringString.get(this, "itemprop")
    set(newValue) {
        attributeStringString.set(this, "itemprop", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.lang: String
    get() = attributeStringString.get(this, "lang")
    set(newValue) {
        attributeStringString.set(this, "lang", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.role: String
    get() = attributeStringString.get(this, "role")
    set(newValue) {
        attributeStringString.set(this, "role", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.runAt: RunAt
    get() = attributeRunAtEnumRunAtValues.get(this, "runat")
    set(newValue) {
        attributeRunAtEnumRunAtValues.set(this, "runat", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.spellCheck: Boolean
    get() = attributeBooleanBoolean.get(this, "spellcheck")
    set(newValue) {
        attributeBooleanBoolean.set(this, "spellcheck", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.style: String
    get() = attributeStringString.get(this, "style")
    set(newValue) {
        attributeStringString.set(this, "style", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.subject: String
    get() = attributeStringString.get(this, "subject")
    set(newValue) {
        attributeStringString.set(this, "subject", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.tabIndex: String
    get() = attributeStringString.get(this, "tabIndex")
    set(newValue) {
        attributeStringString.set(this, "tabIndex", newValue)
    }

var <E> CommonAttributeGroupFacade<E>.title: String
    get() = attributeStringString.get(this, "title")
    set(newValue) {
        attributeStringString.set(this, "title", newValue)
    }


interface FormServerAttributeGroupFacade<E> : Tag<E>

var <E> FormServerAttributeGroupFacade<E>.defaultButton: String
    get() = attributeStringString.get(this, "DefaultButton")
    set(newValue) {
        attributeStringString.set(this, "DefaultButton", newValue)
    }

var <E> FormServerAttributeGroupFacade<E>.defaultFocus: String
    get() = attributeStringString.get(this, "DefaultFocus")
    set(newValue) {
        attributeStringString.set(this, "DefaultFocus", newValue)
    }

var <E> FormServerAttributeGroupFacade<E>.submitDisabledControls: Boolean
    get() = attributeBooleanBoolean.get(this, "SubmitDisabledControls")
    set(newValue) {
        attributeBooleanBoolean.set(this, "SubmitDisabledControls", newValue)
    }


interface InputServerAttributeGroupFacade<E> : Tag<E>

var <E> InputServerAttributeGroupFacade<E>.causesValidation: Boolean
    get() = attributeBooleanBoolean.get(this, "CausesValidation")
    set(newValue) {
        attributeBooleanBoolean.set(this, "CausesValidation", newValue)
    }

var <E> InputServerAttributeGroupFacade<E>.validationGroup: String
    get() = attributeStringString.get(this, "ValidationGroup")
    set(newValue) {
        attributeStringString.set(this, "ValidationGroup", newValue)
    }


interface SelectServerAttributeGroupFacade<E> : Tag<E>

var <E> SelectServerAttributeGroupFacade<E>.dataSourceID: String
    get() = attributeStringString.get(this, "DataSourceID")
    set(newValue) {
        attributeStringString.set(this, "DataSourceID", newValue)
    }

var <E> SelectServerAttributeGroupFacade<E>.dataTextField: String
    get() = attributeStringString.get(this, "DataTextField")
    set(newValue) {
        attributeStringString.set(this, "DataTextField", newValue)
    }

var <E> SelectServerAttributeGroupFacade<E>.dataValueField: String
    get() = attributeStringString.get(this, "DataValueField")
    set(newValue) {
        attributeStringString.set(this, "DataValueField", newValue)
    }


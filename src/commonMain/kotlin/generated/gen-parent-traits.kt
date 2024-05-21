package kotlinx.html


/*******************************************************************************
    DO NOT EDIT
    This file was generated by module generate
*******************************************************************************/

interface ButtonServerCommonFlowInteractivePhrasingGroupFacadeAttributeContent : ButtonServerAttributeGroupFacade, CommonAttributeGroupFacade, CommonAttributeGroupFacadeFlowInteractiveContent, CommonAttributeGroupFacadeFlowInteractivePhrasingContent, FlowInteractiveContent, FlowInteractivePhrasingContent, FlowPhrasingContent, HtmlBlockInlineTag, HtmlBlockTag, HtmlInlineTag {
}

interface CommonAttributeGroupFacadeFlowHeadingContent : CommonAttributeGroupFacade, HeadingContent, HtmlBlockTag {
}

interface CommonAttributeGroupFacadeFlowHeadingPhrasingContent : CommonAttributeGroupFacade, CommonAttributeGroupFacadeFlowHeadingContent, FlowPhrasingContent, HtmlBlockInlineTag, HtmlBlockTag, HtmlInlineTag {
}

interface CommonAttributeGroupFacadeFlowInteractiveContent : CommonAttributeGroupFacade, FlowInteractiveContent, HtmlBlockTag {
}

interface CommonAttributeGroupFacadeFlowInteractivePhrasingContent : CommonAttributeGroupFacade, CommonAttributeGroupFacadeFlowInteractiveContent, FlowInteractiveContent, FlowInteractivePhrasingContent, FlowPhrasingContent, HtmlBlockInlineTag, HtmlBlockTag, HtmlInlineTag {
}

interface CommonAttributeGroupFacadeFlowMetaDataContent : CommonAttributeGroupFacade, FlowMetaDataContent, HtmlBlockTag, HtmlHeadTag {
}

interface CommonAttributeGroupFacadeFlowMetaDataPhrasingContent : CommonAttributeGroupFacade, CommonAttributeGroupFacadeFlowMetaDataContent, FlowMetaDataContent, FlowMetaDataPhrasingContent, FlowPhrasingContent, HtmlBlockInlineTag, HtmlBlockTag, HtmlHeadTag, HtmlInlineTag {
}

interface HtmlBlockInlineTag : CommonAttributeGroupFacade, FlowPhrasingContent, HtmlBlockTag, HtmlInlineTag {
}

interface CommonAttributeGroupFacadeFlowPhrasingSectioningContent : CommonAttributeGroupFacade, CommonAttributeGroupFacadeFlowSectioningContent, FlowPhrasingContent, HtmlBlockInlineTag, HtmlBlockTag, HtmlInlineTag {
}

interface CommonAttributeGroupFacadeFlowSectioningContent : CommonAttributeGroupFacade, HtmlBlockTag, SectioningContent {
}

interface CoreAttributeGroupFacadeFlowMetaDataPhrasingContent : CoreAttributeGroupFacade, FlowMetaDataContent, FlowMetaDataPhrasingContent, FlowPhrasingContent {
}

interface FlowInteractiveContent : FlowContent, InteractiveContent {
}

interface FlowInteractivePhrasingContent : FlowInteractiveContent, FlowPhrasingContent {
}

interface FlowMetaDataContent : FlowContent, MetaDataContent {
}

interface FlowMetaDataPhrasingContent : FlowMetaDataContent, FlowPhrasingContent {
}

interface FlowPhrasingContent : FlowContent, PhrasingContent {
}

interface HtmlBlockTag : CommonAttributeGroupFacade, FlowContent {
}

interface HtmlHeadTag : CommonAttributeGroupFacade, MetaDataContent {
}

interface HtmlInlineTag : CommonAttributeGroupFacade, PhrasingContent {
}


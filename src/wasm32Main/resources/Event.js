konan.libraries.push({
    kotlinx_Event_preventDefault: function (arena, obj) {
        kotlinObject(arena, obj).preventDefault();
    },
    kotlinx_Event_stopPropagation: function (arena, obj) {
        kotlinObject(arena, obj).stopPropagation();
    },
    kotlinx_Event_initEvent: function (arena, obj, typePtr, typeLen, bubbles, cancelable) {
        kotlinObject(arena, obj).initEvent(toUTF16String(typePtr, typeLen, bubbles === 1, cancelable === 1));
    },
});

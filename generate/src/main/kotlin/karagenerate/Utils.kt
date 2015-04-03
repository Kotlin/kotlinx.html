/*
 * Copyright 2010-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kara.generate

import java.util.HashMap


public class Cache<I, R> {
    private val cache: MutableMap<I, R> = HashMap()

    fun get(input: I, foo: I.() -> R): R {
        if (cache.containsKey(input)) {
            return cache.get(input)!!;
        } else {
            val result = input.foo()
            cache.put(input, result)
            return result
        }
    }

    fun getAllElements(): Collection<R> {
        return cache.values()
    }
}

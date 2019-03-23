/*
 * Copyright (c) 2019 Nam Nguyen, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kohii.internal

import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kohii.v1.Playback
import kohii.v1.PlaybackManager
import java.lang.ref.WeakReference

class ViewPager2Container(
  override val container: ViewPager2,
  manager: PlaybackManager
) : ViewContainer<ViewPager2>(container, manager) {

  private val pageChangeCallback by lazy { SimpleOnPageChangeCallback(manager) }

  override fun onAdded() {
    container.registerOnPageChangeCallback(pageChangeCallback)
    manager.dispatchRefreshAll()
  }

  override fun onRemoved() {
    super.onRemoved()
    container.unregisterOnPageChangeCallback(pageChangeCallback)
  }

  override fun allowsToPlay(playback: Playback<*, *>): Boolean {
    return playback.token.shouldPlay()
  }

  override fun accepts(target: Any): Boolean {
    return false
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ViewPager2Container) return false
    if (container != other.container) return false
    return true
  }

  override fun hashCode(): Int {
    return container.hashCode()
  }

  private class SimpleOnPageChangeCallback(manager: PlaybackManager) : OnPageChangeCallback() {
    val weakManager = WeakReference(manager)

    override fun onPageSelected(position: Int) {
      weakManager.get()
          ?.dispatchRefreshAll()
    }

    override fun onPageScrollStateChanged(state: Int) {
      weakManager.get()
          ?.dispatchRefreshAll()
    }
  }
}

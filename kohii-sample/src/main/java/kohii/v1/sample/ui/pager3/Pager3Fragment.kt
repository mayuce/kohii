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

package kohii.v1.sample.ui.pager3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kohii.v1.Kohii
import kohii.v1.Playable
import kohii.v1.ViewTarget
import kohii.v1.sample.DemoApp
import kohii.v1.sample.R
import kohii.v1.sample.common.BaseFragment
import kohii.v1.sample.data.Sources
import kohii.v1.sample.data.Video
import kotlinx.android.synthetic.main.fragment_pager_2_vertical.viewPager
import kotlinx.android.synthetic.main.widget_video_container.view.videoFrame

// ViewPager2 whose pages are Views
class Pager3Fragment : BaseFragment() {

  companion object {
    fun newInstance() = Pager3Fragment()
  }

  class VideoViewHolder(
    val kohii: Kohii,
    itemView: View
  ) : ViewHolder(itemView) {

    fun bind(video: Sources) {
      val itemTag = "$javaClass::$adapterPosition::${video.file}"
      kohii.setUp(video.file)
          .with {
            tag = itemTag
            preLoad = true
            repeatMode = Playable.REPEAT_MODE_ONE
          }
          .bind(ViewTarget(itemView.videoFrame))
    }
  }

  class VideoPagerAdapter(
    val kohii: Kohii,
    private val videos: List<Video>
  ) : Adapter<VideoViewHolder>() {
    override fun onCreateViewHolder(
      parent: ViewGroup,
      viewType: Int
    ): VideoViewHolder {
      val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.widget_video_container, parent, false)
      return VideoViewHolder(kohii, view)
    }

    override fun onBindViewHolder(
      holder: VideoViewHolder,
      position: Int
    ) {
      val video = videos[position % videos.size].playlist.first()
          .sources.first()
      holder.bind(video)
    }

    override fun getItemCount(): Int {
      return Int.MAX_VALUE
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_pager_2_vertical, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    val kohii = Kohii[this]
    kohii.register(this, viewPager, viewPager.getChildAt(0))

    this.viewPager.adapter =
      VideoPagerAdapter(kohii, (requireActivity().application as DemoApp).videos)
  }
}
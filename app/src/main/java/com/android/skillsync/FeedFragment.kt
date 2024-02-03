package com.android.skillsync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.adapters.PostAdapter
import com.android.skillsync.helpers.ActionBarHelper
import com.android.skillsync.models.Post.Post

class FeedFragment : Fragment() {

    private lateinit var posts: ArrayList<Post>;

    private lateinit var postsRecyclerView: RecyclerView;
    private lateinit var postAdapter: PostAdapter;
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_feed, container, false)
        initPosts()

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ActionBarHelper.showActionBarAndBottomNavigationView(requireActivity() as? AppCompatActivity)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun initPosts() {
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView) ?: return
        postsRecyclerView.setHasFixedSize(true)
        postsRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        postAdapter = PostAdapter(getPosts())
        postsRecyclerView.adapter = postAdapter
    }

    private fun getPosts(): ArrayList<Post> {
        posts = ArrayList()

        // TODO: we need to populate the owner details (name + image) from the ownerId
        val defaultPost1 = Post("111", "Wix", "title", "post content here", "")
        val defaultPost2 = Post("222", "Amazon", "title", "Amazon's here! this is out new post and work offers. Follow us for more updates and information", "")
        val defaultPost3 = Post("333", "Fox", "title", "post content here", "")
        posts.add(defaultPost1)
        posts.add(defaultPost2)
        posts.add(defaultPost3)

        return posts
    }
}
package com.android.skillsync

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.adapters.GroupMemberAdapter
import com.android.skillsync.models.Group.FirebaseGroup

class GroupProfileFragment : Fragment() {

    private var profileImageBackgroundElement: ImageView? = null;

    private lateinit var groupMembersRecyclerView: RecyclerView;
    private lateinit var membersList: ArrayList<FirebaseGroup>;
    private lateinit var groupMemberAdapter: GroupMemberAdapter;
    private lateinit var view: View

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_group_profile, container, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Blur the background image
            profileImageBackgroundElement = view.findViewById(R.id.group_profile_background_image);
            profileImageBackgroundElement?.setRenderEffect(
                RenderEffect.createBlurEffect(
                    50f, 50f, Shader.TileMode.CLAMP
                )
            )
        }
        init();

        return view
    }

    private fun init() {
        groupMembersRecyclerView = view.findViewById(R.id.groupMembersRecyclerView) ?: return
        groupMembersRecyclerView.setHasFixedSize(true);
        groupMembersRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        membersList = ArrayList();

        addMembersToList();

        groupMemberAdapter = GroupMemberAdapter(membersList);
        groupMembersRecyclerView.adapter = groupMemberAdapter;
    }

    private fun addMembersToList() {
        membersList.add(
            FirebaseGroup(
                "skillSync",
                "SkillSync@gmail.com",
                "College of managment",
                "we are three talented developers",
                listOf("Hadar", "Amit", "Nofar")
            )
        );
    }
}

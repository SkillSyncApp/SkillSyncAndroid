package com.android.skillsync

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.models.Group


class GroupProfile : AppCompatActivity() {

    private var profileImageBackgroundElement: ImageView? = null;

    private lateinit var groupMembersRecyclerView: RecyclerView;
    private lateinit var membersList:ArrayList<Group>;
    private lateinit var groupMemberAdapter: GroupMemberAdapter;

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_profile)

        // Blue the background image
        profileImageBackgroundElement = findViewById(R.id.group_profile_background_image);
        profileImageBackgroundElement?.setRenderEffect( RenderEffect.createBlurEffect(
            50f, 50f, Shader.TileMode.CLAMP
        ))

        init();
    }

    private fun init() {
        groupMembersRecyclerView = findViewById(R.id.groupMembersRecyclerView);
        groupMembersRecyclerView.setHasFixedSize(true);
        groupMembersRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        membersList = ArrayList();

        addMembersToList();

        groupMemberAdapter = GroupMemberAdapter(membersList);
        groupMembersRecyclerView.adapter = groupMemberAdapter;
    }

    private fun addMembersToList(){
        membersList.add(Group("Amit",null,null,null));
        membersList.add(Group("Hadar",null,null,null));
        membersList.add(Group("Nofar",null,null,null));
    }
}

package com.example.standard.firebasestructure.model.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.TextView;

import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.view.*;

import java.util.*;

public class VenueRecyclerAdapter extends RecyclerView.Adapter<VenueRecyclerAdapter.VenueViewHolder> {

    private List<Venue> venueList;
    private List<User> userList;
    private LayoutInflater inflater;
    private final OnItemClickListener listener;
    private Context context;

    public static class VenueViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewNumAttendees;

        public VenueViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.venueName);
            textViewNumAttendees = itemView.findViewById(R.id.numAttendees);
        }

        public void bind(Venue venue, OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(venue);
                }
            });
        }
    }

    Venue getItem(int id){
        return venueList.get(id);
    }

    public VenueRecyclerAdapter(Context context, List<Venue> venueList, List<User> userList, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.venueList = venueList;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VenueRecyclerAdapter.VenueViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.view_venue_item, viewGroup, false);
        return new VenueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VenueViewHolder venueViewHolder, int i) {
        venueViewHolder.bind(venueList.get(i), listener);
        venueViewHolder.textViewName.setText(venueList.get(i).getVenueName());

        Set<String> friendsAttending = new HashSet<>();
        int j = 0;
        Boolean imGoing = false;

        if(venueList.get(i).getAttendees() != null){
            for(String attendeeId: venueList.get(i).getAttendees().keySet()){
                for(User user: userList){
                    if(attendeeId.equals(user.getUserId())){
                        if(attendeeId.equals(MainApplication.getCurrentUser().getUserId())){
                            imGoing = true;
                        } else {
                            friendsAttending.add(user.getUserName());
                        }
                    }
                }
            }
            String attendeeOrPlural = venueList.get(i).getAttendees().size() == 1 ?
                    context.getString(R.string.attendee) : context.getString(R.string.attendees);

            String otherOrPlural = (venueList.get(i).getAttendees().size() - friendsAttending.size() > 1) ?
                    context.getString(R.string.othersGoingOut) : context.getString(R.string.otherGoingOut);

            if(imGoing){
                venueViewHolder.textViewNumAttendees.append("You, ");
            }

            //all attendees are friends
            if(friendsAttending.size() == venueList.get(i).getAttendees().size()){
                for(String friendAttendingName: friendsAttending){
                    if(++j == friendsAttending.size()){
                        venueViewHolder.textViewNumAttendees.append(friendAttendingName);
                    } else {
                        venueViewHolder.textViewNumAttendees.append(friendAttendingName + ", ");
                    }
                }
            //no attendees are friends
            } else if(friendsAttending.size() == 0){
                if(imGoing){
                    if((venueList.get(i).getAttendees().size() - 1) == 0){
                        venueViewHolder.textViewNumAttendees.setText("You");
                    } else {
                        venueViewHolder.textViewNumAttendees.setText((venueList.get(i).getAttendees().size() - 1) + attendeeOrPlural);
                    }
                } else {
                    venueViewHolder.textViewNumAttendees.setText(venueList.get(i).getAttendees().size() + attendeeOrPlural);
                }
            } else {
                for(String friendAttendingName: friendsAttending){
                    if(++j == friendsAttending.size()){
                        venueViewHolder.textViewNumAttendees.append(friendAttendingName);
                    } else {
                        venueViewHolder.textViewNumAttendees.append(friendAttendingName + ", ");
                    }
                    if(imGoing){
                        if((venueList.get(i).getAttendees().size() - friendsAttending.size()) == 0){
                            venueViewHolder.textViewNumAttendees.append(" and " + (venueList.get(i).getAttendees().size() - friendsAttending.size()) + otherOrPlural);
                        }
                    } else {
                        venueViewHolder.textViewNumAttendees.append(" and " + (venueList.get(i).getAttendees().size() - friendsAttending.size()) + otherOrPlural);
                    }
                }
            }
        } else {
            venueViewHolder.textViewNumAttendees.setText(R.string.noAttendees);
        }
    }

    @Override
    public int getItemCount() {
        if(venueList != null){
            return venueList.size();
        } else {
            return 0;
        }
    }
}


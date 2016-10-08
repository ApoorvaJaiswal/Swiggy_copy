package com.example.user.swiggy_copy;



import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Activity extends Fragment {
    ImageButton imageButton;
    EditText editText;
    List<Card> cardwa = new ArrayList<Card>();
    List<Card> cardSearched = new ArrayList<Card>();
    Card c,c1;
    String title;
    int i;
    String texts[] = {"Supersandwich","SLV Upahara", "Coffee Stop", "Chocoberry"};
    String location[]={"Kumaraswamy Layout","Kumaraswamy Layout","Meenakshi Mall","Meenakshi Mall"};
    String imm[] = new String[4];
    RecyclerView rv;
    private cardAdapter cv,cvSer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewL= inflater.inflate(R.layout.layout_main, container, false);
        rv=(RecyclerView)viewL.findViewById(R.id.card_container);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        title=getActivity().getTitle().toString();//Location from Action Bar
        cv= new cardAdapter(cardwa);
        updateUI();
        imageButton=(ImageButton)viewL.findViewById(R.id.imageButton);
        editText = (EditText) viewL.findViewById(R.id.text);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                if (input.equals("")) {

                    //cv.setCardwas(cardwa);
                    Toast.makeText(getContext(), "Please enter a search string", Toast.LENGTH_SHORT).show();
                }
                else{
                    updateUI_search(input);
                }
                editText.setText("");

            }
        });
        return viewL;
    }


    public void updateUI_search(String st) {
        int j = 0;


        for (i = 0; i < 4; i++) {
            if (patternMatch(st, i)) {
                imm[i] = "img" + i;
                c1 = new Card();
                c1.setImageText(imm[i]);
                c1.setText(texts[i]);
                cardSearched.add(c1);
                j++;
            }

        }
        if (j == 0)
            Toast.makeText(getContext(), "Nothing matches", Toast.LENGTH_SHORT).show();
        else {
            cv.setCardwas(cardSearched);
            cardSearched.clear();

        }
    }



    public boolean patternMatch(String input,int pos){

        String st=location[pos];//the string in which we need to check if the location matches
        String st_up,input_up;
        if(st.equals(input))//input text is as it is equal
            return true;
        else {
            st_up= st.toUpperCase();
            input_up=input.toUpperCase();
            if(st_up.equals(input_up))//input text's case differs but matches
                return true;
            else
                return  false;
        }
    }

    public void updateUI(){
        for(i=0; i<4; i++)
        {
            imm[i] = "img"+i;
            c = new Card();
            c.setImageText(imm[i]);
            c.setText(texts[i]);
            cardwa.add(c);
        }


        //cv.setCardwas(cardwa);
        rv.setAdapter(cv);

    }

    private class cardholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imV;
        TextView txV;

        public cardholder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imV = (ImageView)itemView.findViewById(R.id.image_view);
            txV = (TextView)itemView.findViewById(R.id.text_view);
        }


        @Override
        public void onClick(View v) {
            Intent i= new Intent(getContext(),Blank.class);
            startActivity(i);
        }

        public void setting(Card card){

            imV.setImageResource(getResources().getIdentifier(card.getImageText(), "mipmap", getActivity().getPackageName()));
            txV.setText(card.getText());
        }

    }

    private class cardAdapter extends RecyclerView.Adapter<cardholder>{

        private List<Card> cardwas;//= new ArrayList<Card>();
        public cardAdapter (List<Card> cardwass){
            cardwas = cardwass;
        }
        public void setCardwas(List<Card> cardwass){
            cardwas.clear();
            cardwas.addAll(cardwass);
            notifyDataSetChanged();
        }

        @Override
        public cardholder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater= LayoutInflater.from(getActivity());
            View v= layoutInflater.inflate(R.layout.card,parent,false);
            return new cardholder(v);
        }

        @Override
        public int getItemCount() {
            return cardwas.size();
        }

        @Override
        public void onBindViewHolder(cardholder holder, int position) {
            Card card = cardwa.get(position);
            holder.setting(card);
        }
    }
}

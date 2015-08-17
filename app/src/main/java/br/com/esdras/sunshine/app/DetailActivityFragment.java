package br.com.esdras.sunshine.app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.action_settings){
            Intent intent = new Intent(getActivity(),SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        if(intent != null && intent.hasExtra((Intent.EXTRA_TEXT))){
            String stringExtra = intent.getStringExtra(Intent.EXTRA_TEXT);
            TextView textView = (TextView) view.findViewById(R.id.detail_text);
            textView.setText(stringExtra);
        }

        return view;
    }
}

package com.jpb.music.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jpb.music.R;
import com.jpb.music.activities.SelectedAlbumActivity;
import com.jpb.music.adapter.AlbumsAdapter;
import com.jpb.music.helper.ListHelper;
import com.jpb.music.listener.AlbumSelectListener;
import com.jpb.music.model.Album;
import com.jpb.music.viewmodel.MainViewModel;
import com.jpb.music.viewmodel.MainViewModelFactory;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class AlbumsFragment extends Fragment implements AlbumSelectListener, SearchView.OnQueryTextListener {

    private final List<Album> albumList = new ArrayList<>();
    private AlbumsAdapter albumsAdapter;
    private List<Album> unchangedList = new ArrayList<>();
    private MainViewModel viewModel;

    private MaterialToolbar toolbar;
    private SearchView searchView;

    public AlbumsFragment() {
    }

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity(),
                new MainViewModelFactory(requireActivity())).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        unchangedList = viewModel.getAlbums(false);
        albumList.clear();
        albumList.addAll(unchangedList);

        toolbar = view.findViewById(R.id.search_toolbar);

        RecyclerView recyclerView = view.findViewById(R.id.albums_layout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        albumsAdapter = new AlbumsAdapter(albumList, this);
        recyclerView.setAdapter(albumsAdapter);

        setUpOptions();
        return view;
    }

    private void setUpOptions() {
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.search) {
                searchView = (SearchView) item.getActionView();
                setUpSearchView();
                return true;
            } else if (id == R.id.menu_sort_asc) {
                updateAdapter(ListHelper.sortAlbumByName(albumList, false));
                return true;
            } else if (id == R.id.menu_sort_dec) {
                updateAdapter(ListHelper.sortAlbumByName(albumList, true));
                return true;
            } else if (id == R.id.menu_most_songs) {
                updateAdapter(ListHelper.sortAlbumBySongs(albumList, false));
                return true;
            } else if (id == R.id.menu_least_songs) {
                updateAdapter(ListHelper.sortAlbumBySongs(albumList, true));
                return true;
            } else if (id == R.id.menu_longest_dur) {
                updateAdapter(ListHelper.sortAlbumByDuration(albumList, false));
                return true;
            } else if (id == R.id.menu_shortest_dur) {
                updateAdapter(ListHelper.sortAlbumByDuration(albumList, true));
                return true;
            }

            return false;
        });
        toolbar.setNavigationOnClickListener(v -> {
            if (searchView == null || searchView.isIconified())
                requireActivity().finish();
        });
    }

    private void setUpSearchView() {
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        updateAdapter(ListHelper.searchByAlbumName(unchangedList, query.toLowerCase()));
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        updateAdapter(ListHelper.searchByAlbumName(unchangedList, newText.toLowerCase()));
        return true;
    }

    private void updateAdapter(List<Album> list) {
        albumList.clear();
        albumList.addAll(list);
        albumsAdapter.notifyDataSetChanged();
    }

    @Override
    public void selectedAlbum(Album album) {
        requireActivity().startActivity(new Intent(
                getActivity(),
                SelectedAlbumActivity.class
        ).putExtra("album", album));
    }
}
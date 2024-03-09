package com.theost.rxapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.theost.rxapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ActivityMainBinding binding;
    private EditText editText;
    private String text;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(view -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.button.setVisibility(View.GONE);
            String text = binding.editText.getText().toString();
            compositeDisposable.add(
                    Api.getData()
                            .map(apiObjects -> apiObjects.stream()
                                    .filter(apiObject -> apiObject.getValue().contains(text))
                                    .sorted(Comparator.comparing(ApiObject::getValue))
                                    .limit(100)
                                    .collect(Collectors.toList())
                            )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                            apiObjects -> {
                                String result = apiObjects.stream()
                                        .map(ApiObject::getValue)
                                        .collect(Collectors.joining(", "));
                                binding.textView.setText(result);
                                binding.progressBar.setVisibility(View.GONE);
                                binding.button.setVisibility(View.VISIBLE);
                            },
                            throwable -> {throwable.printStackTrace();}
                    )
            );
        });}
        @Override
        protected void onDestroy() {
            super.onDestroy();
            compositeDisposable.clear();
        }
    }


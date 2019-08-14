package edu.uta.leanmed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.services.SharedPreferenceService;

public class InfoFragment extends Fragment {
    private CardView mLangPref,mContact,mPassword, mInfo, mLogout;
    private View mLanguageDailog, mContactDailog, mPasswordDailog;
    private TextView mName,mType;
    public InfoFragment() {
    }
    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_info, container, false);
        mLangPref=view.findViewById(R.id.langPref);
        mContact=view.findViewById(R.id.contact);
        mPassword=view.findViewById(R.id.password);
        mInfo=view.findViewById(R.id.info);
        mLogout=view.findViewById(R.id.logout);
        mName=view.findViewById(R.id.name);
        mType=view.findViewById(R.id.description);
        final User user =(User) getActivity().getIntent().getSerializableExtra("user");
        mName.setText(user.getUserName());
        switch (user.getType()){
            case 1:
                mType.setText(getString(R.string.prompt_getDon));
                break;
            case 2:
                mType.setText(getString(R.string.prompt_recDon));
                break;
            case 3:
                mType.setText(getString(R.string.prompt_admin));
                break;
        }
        AlertDialog.Builder mLangPrefBuilder=new AlertDialog.Builder(getActivity());
        mLanguageDailog=getLayoutInflater().inflate(R.layout.dailog_language_pref, null);
        RadioButton engLangPref=mLanguageDailog.findViewById(R.id.langEng);
        RadioButton spanishLangPref=mLanguageDailog.findViewById(R.id.langSpanish);
        final RadioGroup langPrefGroup=mLanguageDailog.findViewById(R.id.select_lang_pref);
        if(user.getLanguagePref()==1)   engLangPref.setChecked(true);
        else    spanishLangPref.setChecked(true);
        ImageView cancelLangDailog=mLanguageDailog.findViewById(R.id.dailog_close);
        mLangPrefBuilder.setView(mLanguageDailog);
        final AlertDialog languageDailog=mLangPrefBuilder.create();
        cancelLangDailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageDailog.dismiss();
            }
        });
        Button selectPref=mLanguageDailog.findViewById(R.id.buttonSelect);
        selectPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton selectedButton=mLanguageDailog.findViewById(langPrefGroup.getCheckedRadioButtonId());
                if(selectedButton.getText().toString().equalsIgnoreCase(getString(R.string.english)))
                    user.setLanguagePref(1);
                else user.setLanguagePref(2);
                SharedPreferenceService.setLocale(getActivity().getBaseContext(), user.getLanguagePref()==1?"en":"es");
                SharedPreferenceService.saveObjectToSharedPreference(getActivity(), user.getEmailId(), user);
                SharedPreferenceService.loadLocale(getActivity().getBaseContext());
                getFragmentManager().beginTransaction().replace(R.id.forgotPwContent, InfoFragment.newInstance()).commit();
                languageDailog.dismiss();
            }
        });
        mLangPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageDailog.show();
            }
        });

        AlertDialog.Builder mContactBuilder=new AlertDialog.Builder(getActivity() );
        mContactDailog=getLayoutInflater().inflate(R.layout.dailog_contact, null);
        ImageView cancelContactDailog=mContactDailog.findViewById(R.id.dailog_close);
        mContactBuilder.setView(mContactDailog);
        final AlertDialog contactDailog=mContactBuilder.create();
        cancelContactDailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactDailog.dismiss();
            }
        });
        mContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactDailog.show();
            }
        });

        AlertDialog.Builder mPassBuilder=new AlertDialog.Builder(getActivity());
        mPasswordDailog=getLayoutInflater().inflate(R.layout.dailog_password, null);
        ImageView cancelPassDailog=mPasswordDailog.findViewById(R.id.dailog_pass_close);
        Button updatePass=mPasswordDailog.findViewById(R.id.buttonSelect);
        mPassBuilder.setView(mPasswordDailog);
        final AlertDialog passwordDailog=mPassBuilder.create();
        cancelPassDailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordDailog.dismiss();
            }
        });
        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordDailog.dismiss();
            }
        });

        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordDailog.show();
            }
        });
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),InfoActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext(),LoginActivity.class);
                SharedPreferenceService.removePreferences(getActivity().getApplicationContext());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                getActivity().finish();
            }
        });
        return view;
    }

}

package com.codewithpk.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeFragment extends Fragment implements DatePickerFragment.Callbacks {
    private static final String DATE_FORMAT = "EEE, MMM, dd";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    //add a constant for the request code
    private static final int REQUEST_CONTACT = 1;
    //Grabbing the photo file location
    private File mPhotoFile;
    //Adding a photo URI property
    private Uri mPhotoUri;

    private static final int REQUEST_PHOTO = 2;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String TAG = "CrimeFragment";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    //member variable for the SEND CRIME REPORT  button.
    private Button mReportButton;
    //member variable for the CHOOSE SUSPECT button.
    private Button mPersonButton;
    private CheckBox mSolvedCheckBox;
    private CrimeDetailViewModel mCrimeDetailViewModel;
//to respond to presses on your ImageButton and to control the content of your ImageView,
// you need properties referring to each of them
    //Adding properties
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    //Writing a newInstance(UUID) function
    /*write a newInstance(UUID) function that accepts a UUID,
     creates an arguments bundle, creates a fragment instance,
     and then attaches the arguments to the fragment */

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
    //Hooking CrimeFragment up to CrimeDetailViewModel
        ViewModelProvider provider = ViewModelProviders.of(this);
        mCrimeDetailViewModel = provider.get(CrimeDetailViewModel.class);
        mCrimeDetailViewModel.initData(this.getContext());
        //Getting crime ID from the arguments
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        Log.d(TAG, "args bundle crime ID: " + crimeId);
        // Eventually, load crime from database
        mCrimeDetailViewModel.loadCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        //calling findViewById(Int) as usual on your inflated fragment_crime.xml to find your new views and wire them up
        //calling on Added properties to find view
        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_camera);
        mPhotoView = (ImageView)v.findViewById(R.id.crime_photo);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
// This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
// This space intentionally left blank
            }
        });
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        //mDateButton.setEnabled(false);
        //Showing your DialogFragment
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatePickerFragment dateFragment = new DatePickerFragment();
                //Adding a call to newInstance(…)
                DatePickerFragment dateFragment = DatePickerFragment.newInstance(mCrime.getDate());
                //create a constant for the request code and then make CrimeFragment the target fragment of the DatePickerFragment instance  #Constant line 26
                dateFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dateFragment.show(requireFragmentManager(), DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        /* Sending a crime report, get a reference to the SEND CRIME REPORT button,
        then set a listener on it. Within the listener’s implementation, create an
        implicit intent and pass it into startActivity(Intent) */
        mReportButton = (Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                //startActivity(i);
                //Create a chooser to display the activities that respond to your implicit intent.
                Intent chooserIntent = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(chooserIntent);
            }
        });
        /* Create an implicit intent for requesting a contact. Also,
        if a suspect is assigned, show the name on the CHOOSE SUSPECT button */
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mPersonButton = (Button) v.findViewById(R.id.crime_suspect);
        mPersonButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

/*Some devices or users may not have a contacts app. This is a problem,
 because if the OS cannot find a matching activity, then the app will crash.
  The fix is to check with part of the OS called the PackageManager first
 */
        //pickContact.addCategory(Intent.CATEGORY_HOME);
        PackageManager packageManager = requireActivity().getPackageManager();
        /*write an implicit intent to ask for a new picture to be taken into the location saved in
        photo Uri */
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                List<ResolveInfo> cameraActivities =
                        packageManager.queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo cameraActivity: cameraActivities) {
                    requireActivity().grantUriPermission(
                            cameraActivity.activityInfo.packageName, mPhotoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mPersonButton.setEnabled(false);
        }
        /*
        if (packageManager.resolveActivity(captureImage,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mPhotoButton.setEnabled(false);
        } */
        return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCrimeDetailViewModel.mCrimeLiveData.observe(this.getViewLifecycleOwner(), new Observer<Crime>() {
            @Override
            public void onChanged(@Nullable final Crime crime) {
                mCrime = crime;
                //Start by stashing or grabbing the location of the photo file
                mPhotoFile = mCrimeDetailViewModel.getPhotoFile(crime);
                //new property for the photo URI and initialize it after you have a reference to the photo
                mPhotoUri = FileProvider.getUriForFile(requireActivity(),
                        "com.codewithpk.criminalintent.fileprovider", mPhotoFile);
                updateUI();
            }
        });
    }
    //update CrimeFragment to save the user’s edited crime data to the database
    @Override
    public void onStop() {
        super.onStop();
        mCrimeDetailViewModel.saveCrime(mCrime);
    }
    //implement the Callbacks interface in CrimeFragment. In onDateSelected(), set the date on the crime property and update the UI
    @Override
    public void onDateSelected(Date date) {
        mCrime.setDate(date);
        updateUI();
    }
    private void updateUI() {
        mTitleField.setText(mCrime.getTitle());
        mDateButton.setText(mCrime.getDate().toString());
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        //code to skip the checkbox animation
        mSolvedCheckBox.jumpDrawablesToCurrentState();
//modifying updateUI() to set the text on the CHOOSE SUSPECT button if the crime has a suspect
        if (!mCrime.getSuspect().equals("")) {
            mPersonButton.setText(mCrime.getSuspect());
        }
        // calling updatePhotoView function from inside updateUI() and onActivityResult(…)
        updatePhotoView();
    }
    // to load this Bitmap into ImageView, need to ADD a function to CrimeFragment to update the photo view

    private void updatePhotoView() {
        if (mPhotoFile.exists()) {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), requireActivity());
            mPhotoView.setImageBitmap(bitmap);
        } else {
            mPhotoView.setImageDrawable(null);
        }
    }
    //add a function that creates four strings and then pieces them together and returns a complete report
    private String getCrimeReport() {
        String solvedString = mCrime.isSolved() ? getString(R.string.crime_report_solved) : getString(R.string.crime_report_unsolved);
        String dateString = DateFormat.format(DATE_FORMAT, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect().isEmpty() ? getString(R.string.crime_report_no_suspect) : getString(R.string.crime_report_suspect, mCrime.getSuspect());
        return getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
    }
//Revoking URI permissions
    @Override
    public void onDetach() {
        super.onDetach();
        requireActivity().revokeUriPermission(mPhotoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }
//implement onActivityResult(…) to retrieve the contact’s name from the contacts application
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            Cursor c = requireActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            try {
                if (c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                String person = c.getString(0);
                mCrime.setSuspect(person);
                mCrimeDetailViewModel.saveCrime(mCrime);
                mPersonButton.setText(person);
            } finally {
                c.close();
            }
           // calling updatePhotoView function from inside updateUI() and onActivityResult(…)
            if (requestCode == REQUEST_PHOTO) {
                updatePhotoView();
                //Revoking URI permissions
                requireActivity().revokeUriPermission(mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
    }
}

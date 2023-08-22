package co.il.shivhit.mvvm_blogs.ACTIVITIES;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import co.il.shivhit.helper.AlertUtil;
import co.il.shivhit.helper.DateUtil;
import co.il.shivhit.helper.inputValidators.DateRule;
import co.il.shivhit.helper.inputValidators.EntryValidation;
import co.il.shivhit.helper.inputValidators.NameRule;
import co.il.shivhit.helper.inputValidators.Rule;
import co.il.shivhit.helper.inputValidators.RuleOperation;
import co.il.shivhit.helper.inputValidators.TextRule;
import co.il.shivhit.helper.inputValidators.Validator;
import co.il.shivhit.model.BlogPost;
import co.il.shivhit.mvvm_blogs.R;

public class BlogPostActivity extends BaseActivity implements EntryValidation {
    private EditText etAuthor;
    private EditText  etTitle;
    private EditText  etContent;
    private EditText  etDate;
    private ImageView ivCalendar;
    private Button    btnSave;
    private Button    btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_post);

        initializeViews();
        setValidation();    }

    @Override
    protected void initializeViews() {
        etAuthor    = findViewById(R.id.etAuthor);
        etTitle     = findViewById(R.id.etTitle);
        etDate      = findViewById(R.id.etDate);
        ivCalendar  = findViewById(R.id.ivCalendar);
        etContent   = findViewById(R.id.etContent);
        btnSave     = findViewById(R.id.btnSave);
        btnCancel   = findViewById(R.id.btnCancel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            etDate.setText(DateUtil.locaDateToString(LocalDate.now(), "dd/MM/uuuu"));
        }

        setListeners();
    }

    @Override
    protected void setListeners() {
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Select date");
                builder.setTextInputFormat(new SimpleDateFormat("dd/MM/yyyy"));

                CalendarConstraints constraint = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    constraint = DateUtil.buidCalendarConstrains(LocalDate.now().minusDays(10), LocalDate.now());
                }
                builder.setCalendarConstraints(constraint);

                if (!etDate.getText().toString().isEmpty())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDateTime date = LocalDate.parse(etDate.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                        ZonedDateTime zdt = ZonedDateTime.of(date, ZoneId.systemDefault());
                        builder.setSelection(zdt.toInstant().toEpochMilli());
                    }

                MaterialDatePicker picker = builder.build();

                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            etDate.setText(Instant.ofEpochMilli((long) selection).atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            etDate.setError(null);
                        }
                        else
                            etDate.setText("ERROR !!!");
                    }
                });

                picker.addOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        AlertUtil.alertOk(BlogPostActivity.this, "Date", "Please enter post date", true, R.drawable.exclamation_mark);
                    }
                });

                picker.setCancelable(true);
                picker.show(getSupportFragmentManager(), "DATE PICKER");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    BlogPost blogPost = new BlogPost();
                    blogPost.setAuthor(etAuthor.getText().toString());
                    blogPost.setTitle(etTitle.getText().toString());
                    blogPost.setDate(DateUtil.stringDateToLong(etDate.getText().toString().toString()));
                    blogPost.setContent(etContent.getText().toString());

                    Toast.makeText(BlogPostActivity.this, " - OK - ", Toast.LENGTH_SHORT).show();
                    //blogViewModel.save(blogPost);

                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    public void setValidation() {
        Validator.add(new Rule(etAuthor, RuleOperation.REQUIRED, "Please enter author name"));
        Validator.add(new NameRule(etAuthor, RuleOperation.NAME, "Author name is wrong"));
        Validator.add(new Rule(etTitle, RuleOperation.REQUIRED, "Please enter title"));
        Validator.add(new TextRule(etTitle, RuleOperation.TEXT, "Title is wrong", 4, 50, true));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Validator.add(new DateRule(etDate, RuleOperation.DATE, "Wrong date", LocalDate.now().minusDays(10), LocalDate.now()));
        };
        Validator.add(new Rule(etContent, RuleOperation.REQUIRED, "Please enter content"));
        Validator.add(new TextRule(etContent, RuleOperation.TEXT, "Wrong content", 10, 1000, true));
    }

    @Override
    public boolean validate() {
        return Validator.validate();
    }
}
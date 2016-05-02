package com.snapstory.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.snapstory.R;
import com.snapstory.util.DataProvider;

public class QuestionListAdapter extends CursorAdapter {
    /*private Context context;
    //private Retriever retriever;
    private int currentlyFocusedView;
    private List<String> answers = new ArrayList<>();
    private String storyID;


    public QuestionListAdapter(Context context, Retriever retriever,List<String> answers,String storyID) {
        this.context = context;
        this.retriever = retriever;
        for(int i=0; i< retriever.getCount(); i++) {
            answers.add("");
        }
        this.answers=answers;
        this.storyID=storyID;
    }

    @Override
    public int getCount() {
        return retriever == null ? 0 : retriever.getCount();
    }

    @Override
    public Object getItem(int position) {
        retriever.moveToPosition(position);
        return questionTable().getApi().id(retriever);
    }

    public long getQuestionID(int position) {
        retriever.moveToPosition(position);
        return retriever == null ? -1 : questionTable().getApi().id(retriever);
    }

    public String getQuestionType(int position) {
        retriever.moveToPosition(position);
        return retriever == null ? "" : questionTable().getApi().type(retriever);
    }

    @Override
    public long getItemId(int position) {
        return retriever == null || getCount() <= position ? -1L : position;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getAnswer(int position) {
        if(!answers.isEmpty() && !Strings.isNullOrEmpty(answers.get(position))) {
            return answers.get(position);
        }

        return "";
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        final int position = pos;
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.quesion_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.question = ((TextView)convertView.findViewById(R.id.question));
            viewHolder.answer = ((EditText)convertView.findViewById(R.id.answer));
            viewHolder.choiceSpinner = ((Spinner)convertView.findViewById(R.id.answer_selector));
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (retriever == null || !retriever.moveToPosition(position)) {
            return null;
        }

        QuestionTable questionTable = questionTable().getApi();



        Retriever answerData = answerTable().find().byQuestionId(questionTable.id(retriever)).andFinally().get();
        if(answerData != null && answerData.moveToFirst()) {
            do {
                System.out.println("answer-->" + answerTable().getApi().answer(answerData));
                //answers.add(answerTable().getApi().answer(answerData));
            } while (answerData.moveToNext());
            answerData.close();
        }

        StringTokenizer tokenizer = new StringTokenizer(questionTable.choices(retriever), "/");
        List<String> choices = new ArrayList<>();
        while(tokenizer.hasMoreTokens()) {
            choices.add(tokenizer.nextToken());
        }

        viewHolder.question.setText(questionTable.question(retriever));

        if(choices.isEmpty()) {
            viewHolder.answer.setText(getAnswer(position));
            viewHolder.answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        currentlyFocusedView =position;
                    }
                }
            });

            if(position == currentlyFocusedView) {
                viewHolder.answer.requestFocus();
            }

            viewHolder.answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    answers.set(position, s.toString());
                }
            });

            viewHolder.answer.setVisibility(View.VISIBLE);
            viewHolder.choiceSpinner.setVisibility(View.GONE);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, choices);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewHolder.choiceSpinner.setAdapter(adapter);
            viewHolder.choiceSpinner.setVisibility(View.VISIBLE);
            viewHolder.answer.setVisibility(View.GONE);


            if(!answers.isEmpty()) {
                int selectedAnswerPosition = choices.indexOf(answers.get(position));
                if(selectedAnswerPosition != -1) {
                    viewHolder.choiceSpinner.setSelection(selectedAnswerPosition);
                }
            }

            answers.set(position, (String)viewHolder.choiceSpinner.getSelectedItem());
            viewHolder.choiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int choicePosition, long id) {
                    answers.set(position, parent.getItemAtPosition(choicePosition).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        return convertView;
    }*/
	
	private Context context;	
	Cursor cs;
	private List<String> answers = new ArrayList<String>();
	private int currentlyFocusedView;

	@SuppressWarnings("deprecation")
	public QuestionListAdapter(Context context, Cursor c,List<String> answers) {
		super(context, c);
		this.context=context;
		cs = c;
		this.answers=answers;
	}
	
	public int getCount() {			
		return super.getCount();
	}

	public Object getItem(int position) {		
		return null;
	}

	public long getItemId(int position) {			
		return super.getItemId(position);
	}
	
	public String getAnswer(int position) {
        if(!answers.isEmpty() && !(answers.get(position)!=null)) {
            return answers.get(position);
        }

        return "";
    }

	public View getView(final int position, View convertView,
			ViewGroup parent) {		
		ViewHolder holder;
		if (convertView == null) {
			convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.quesion_list_item, null);
			holder = new ViewHolder();
			holder.question = ((TextView)convertView.findViewById(R.id.question));
			holder.answer = ((EditText)convertView.findViewById(R.id.answer));
			holder.choiceSpinner = ((Spinner)convertView.findViewById(R.id.answer_selector));
	        convertView.setTag(holder);
			/*holder.name = (TextView) convertView.findViewById(R.id.txtName);
			holder.time = (TextView) convertView.findViewById(R.id.txtTime);
			holder.message = (TextView) convertView.findViewById(R.id.txtMessage);
			holder.imgTop = (ImageView)convertView.findViewById(R.id.imgTop);
			holder.imgBot = (ImageView)convertView.findViewById(R.id.imgBot);
			holder.layCenter = (View) convertView.findViewById(R.id.layCenter);		
			holder.layListItem = (View) convertView.findViewById(R.id.layListItem);*/
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		cs.moveToPosition(position);	
		
		System.out.println("Choices-->"+cs.getString(cs.getColumnIndex(DataProvider.CHOICES)));
		StringTokenizer tokenizer=null;
		List<String> choices = new ArrayList<String>();
		if(cs.getString(cs.getColumnIndex(DataProvider.CHOICES))!=null){
			tokenizer = new StringTokenizer(cs.getString(cs.getColumnIndex(DataProvider.CHOICES)), "/");
			
	        while(tokenizer.hasMoreTokens()) {
	            choices.add(tokenizer.nextToken());
	        }
		}
        holder.question.setText(cs.getString(cs.getColumnIndex(DataProvider.QUESTION)));
		
        if(choices.isEmpty()) {
        	holder.answer.setText(getAnswer(position));
        	holder.answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        currentlyFocusedView =position;
                    }
                }
            });

            if(position == currentlyFocusedView) {
            	holder.answer.requestFocus();
            }

            holder.answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    answers.set(position, s.toString());
                }
            });

            holder.answer.setVisibility(View.VISIBLE);
            holder.choiceSpinner.setVisibility(View.GONE);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, choices);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.choiceSpinner.setAdapter(adapter);
            holder.choiceSpinner.setVisibility(View.VISIBLE);
            holder.answer.setVisibility(View.GONE);


            if(!answers.isEmpty()) {
                int selectedAnswerPosition = choices.indexOf(answers.get(position));
                if(selectedAnswerPosition != -1) {
                	holder.choiceSpinner.setSelection(selectedAnswerPosition);
                }
            }

            answers.set(position, (String)holder.choiceSpinner.getSelectedItem());
            holder.choiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int choicePosition, long id) {
                    answers.set(position, parent.getItemAtPosition(choicePosition).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
		
	   return convertView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

    private class ViewHolder {
        TextView question;
        EditText answer;
        Spinner choiceSpinner;
    }

	public List<String> getAnswers() {
		return this.answers;
	}

	public String getQuestionID(int i) {		
		
		cs.moveToPosition(i);
		System.out.println("question-->"+i+">>"+cs.getString(cs.getColumnIndex(DataProvider.QUESTION_ID)));
		return cs.getString(cs.getColumnIndex(DataProvider.QUESTION_ID));
	}
}

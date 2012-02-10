package edusoft.android.report;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import edusoft.android.database.DatabaseHelper;
import edusoft.android.main.R;
import edusoft.android.reuse.Utility;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReportActivity extends Activity {
	
	private static final String[] month = {"January","February","March","April","May","June","July","August","September","October","November","December"};
	private static final String[] year = {new Utility().getCurrentYear(),Integer.toString(Calendar.getInstance().get(Calendar.YEAR)-1)};
	private static ArrayAdapter adapterCategory;
	private static List<PieDetailsItem> PieData ;
	boolean isCheck = true;
	
    double income;
    double expense;
    PieDetailsItem Item ;
	DatabaseHelper dbHelp;
	RadioButton radMonth;
	RadioButton radYear;
	Spinner spinnerCategory;
	ImageView imgGraph;
	TextView txtIncome;
	TextView txtExpense;
	TextView txtOverPaid;
		
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
	        setContentView(R.layout.report_main);
	        onCreateInitailLayout();
	        radMonth.setChecked(true);
	        radMonth.setOnClickListener(radMonthOnclick);
	
	        radYear.setChecked(false);
	        radYear.setOnClickListener(radYearOnclick);
	        
	        adapterCategory = new ArrayAdapter(this,android.R.layout.simple_spinner_item, month);
	        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinnerCategory.setAdapter(adapterCategory);
	        spinnerCategory.setSelection(Integer.parseInt(new Utility().getCurrentMonth()));
	        spinnerCategory.setOnItemSelectedListener(categorySpinner_listenner);	           
	        // �Ѵ�ٻ�����ҡ����Ҵ���ʴ�
	        imgGraph.setBackgroundColor(0xff000000);     
	        imgGraph.setImageBitmap(getCirclePie(getReportDeatailWithMonth(Integer.parseInt(new Utility().getCurrentMonth()))));
        }catch (Exception e) {
        	AlertDialog.Builder b = new AlertDialog.Builder(ReportActivity.this);
			b.setMessage(e.toString());
			b.show();
		}

    }
  //  
  //======================================= initial layout ===========================================
  	private void onCreateInitailLayout()
  	{
  		radMonth = (RadioButton) findViewById(R.id.report_main_radMonth);
  		radYear = (RadioButton) findViewById(R.id.report_main_radYear);
  		spinnerCategory = (Spinner) findViewById(R.id.report_main_spinnerCategory);
  		imgGraph =  (ImageView) findViewById(R.id.report_main_imgGraph);
  		txtIncome  =  (TextView) findViewById(R.id.report_main_txtIncome);
  		txtExpense = (TextView) findViewById(R.id.report_main_txtExpense);
  		txtOverPaid = (TextView) findViewById(R.id.report_main_txtOver);
  		dbHelp = new DatabaseHelper(this);
  	}
  //======================================= Listenner ================================================
  	//��÷ӧҹ���� radMonth
	private OnClickListener radMonthOnclick = new OnClickListener() {
		public void onClick(View view) {		
			isCheck = true;
			circlePieChangeFromRadio(month,isCheck);
		}
	};
    //��÷ӧҹ���� radYear
  	private OnClickListener radYearOnclick = new OnClickListener() {
  		public void onClick(View view) {
  			isCheck = false;
  			circlePieChangeFromRadio(year,isCheck);
  		}
  	}; 	
  	// Spinner �ͧ�ѭ�շ�����͡
 	private AdapterView.OnItemSelectedListener categorySpinner_listenner = new AdapterView.OnItemSelectedListener() {
 		public void onItemSelected(AdapterView<?> parent,
 				View view, int pos, long id) {	
 			imgGraph.setBackgroundColor(0xff000000);
 			if(isCheck)
 				imgGraph.setImageBitmap(getCirclePie(getReportDeatailWithMonth(pos)));	
 			else
 				imgGraph.setImageBitmap(getCirclePie(getReportDeatailWithYear(Integer.parseInt(spinnerCategory.getSelectedItem().toString()))));	
 		}

 		public void onNothingSelected(AdapterView<?> parent) {
 		}
 	};
  //==================================== Method ��������� Reuse ���� Class ReportActivity =============================	
 	//����¹��ҿ��ѧ�ҡ���꡷��  radMonth , radYear
 	public void circlePieChangeFromRadio(String[] radData,boolean isCheck)
 	{
		adapterCategory = new ArrayAdapter(this,android.R.layout.simple_spinner_item, radData);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);
        spinnerCategory.setOnItemSelectedListener(categorySpinner_listenner);
        if(isCheck){
        	spinnerCategory.setSelection(Integer.parseInt(new Utility().getCurrentMonth()));
        	// �Ѵ�ٻ�����ҡ����Ҵ���ʴ�
            imgGraph.setBackgroundColor(0xff000000);     
            imgGraph.setImageBitmap(getCirclePie(getReportDeatailWithMonth(Integer.parseInt(new Utility().getCurrentMonth()))));
 		}else
 		{
 			spinnerCategory.setSelection(0);
 			// �Ѵ�ٻ�����ҡ����Ҵ���ʴ�
 	        imgGraph.setBackgroundColor(0xff000000);     
 	        imgGraph.setImageBitmap(getCirclePie(getReportDeatailWithYear(Integer.parseInt(new Utility().getCurrentYear()))));
 		}	      
 	}
 	//�Ӣ����Ũҡ database �ͧ����Ѻ-��¨��� ������͹ �������Ѵŧ� List ���͹���Ҵ�繡�ҿ����
 	public List<PieDetailsItem> getReportDeatailWithMonth(int month)
 	{
 		PieData = new ArrayList<PieDetailsItem>(0);
 		Item = new PieDetailsItem();;
        int MaxPieItems = 2;   
        //�Ӥ�� income ��� expense ������ ArrayList ��������Ҵ�ٻ��ҿ�ա��
        for (int i = 0; i < MaxPieItems ; i++) {
        	Item       = new PieDetailsItem();
        	if(i==0){
        		income = Double.parseDouble(dbHelp.getIncomeWithMonth(Integer.toString(month+1)));
        		Item.Count = income;
        		Item.Color = Color.rgb(42, 127, 4);//0xff00ff00;
        	}else if(i == 1) {
        		expense  = Double.parseDouble(dbHelp.getExpenseWithMonth(Integer.toString(month+1)));
        		Item.Count = expense;
        		Item.Color = Color.rgb(250, 0, 0);//0xffff0000;
        	}
        	PieData.add(Item);
        }
        
        Item       = new PieDetailsItem();
        if((income - expense) <  0) 		Item.Count = expense - income;
        else 								Item.Count = 0;
        
        Item.Color = Color.rgb(255, 96, 0);
        PieData.add(Item);  

        return PieData;
 	}
 	//�Ӣ����Ũҡ database �ͧ����Ѻ-��¨��� ������͹ �������Ѵŧ� List ���͹���Ҵ�繡�ҿ����
 	public List<PieDetailsItem> getReportDeatailWithYear(int year)
 	{
 		PieData = new ArrayList<PieDetailsItem>(0);
 		Item = new PieDetailsItem();;
        int MaxPieItems = 2;   
        //�Ӥ�� income ��� expense ������ ArrayList ��������Ҵ�ٻ��ҿ�ա��
        for (int i = 0; i < MaxPieItems ; i++) {
        	Item       = new PieDetailsItem();
        	if(i==0){
        		income = Double.parseDouble(dbHelp.getIncomeWithYear(Integer.toString(year)));
        		Item.Count = income;
        		Item.Color = Color.rgb(42, 127, 4);//0xff00ff00;
        	}else if(i == 1) {
        		expense  = Double.parseDouble(dbHelp.getExpenseWithYear(Integer.toString(year)));
        		Item.Count = expense;
        		Item.Color = Color.rgb(250, 0, 0);//0xffff0000;
        	}
        	PieData.add(Item);
        }
        
        Item       = new PieDetailsItem();
        if((income - expense) <  0) 		Item.Count = expense - income;
        else 								Item.Count = 0;
        
        Item.Color = Color.rgb(255, 96, 0);
        PieData.add(Item);  

        return PieData;
 	}
 	//��Ң����Ũҡ database ����Ѵŧ� List ���Ҵ�ٻ��ҿ
 	public Bitmap getCirclePie(List<PieDetailsItem> item)
 	{
        //��Ҵ�ͧ ��ҿǧ���
        int Size = 105;
        //�բͧ background �ͧ�ٻ 㹷�������մ�
        int BgColor = 0xff000000;  
        //��ǹ����红����Ţͧ��ҿ���Ҵ�ٻ
        Bitmap mBackgroundImage = Bitmap.createBitmap(Size, Size, Bitmap.Config.RGB_565);
        // ���¡��÷ӧҹ�ͧ�Ҵ�ٻ��ҿ���ʴ�
        View_PieChart PieChartView = new View_PieChart( this );
        PieChartView.setLayoutParams(new LayoutParams(Size, Size));
        PieChartView.setGeometry(Size, Size, 2, 2, 2, 2);
        PieChartView.setSkinParams(BgColor);
        PieChartView.setData(item, income);
        PieChartView.invalidate();
        //�Ҵ�ٻ���� return �ٻ�Ҿ����� bitmap �͡�
        PieChartView.draw(new Canvas(mBackgroundImage));
        PieChartView = null;
        
        if(item.get(0).Count > 0)
        	calculatePercent(item);
        else
        	setTextWithNoData();
        
        return mBackgroundImage;
 	}
 	//���ӹǹ percent ����Ѻ�ʴ��
 	public void calculatePercent(List<PieDetailsItem> item)
 	{
    	String LblPercent;
    	float Percent;

    	DecimalFormat FloatFormatter = new DecimalFormat("0.## %");
    	for (int i = 0; i < item.size(); i++) {
    	    Item = (PieDetailsItem) item.get(i);
    	    Percent = (float)Item.Count / (float)income;
    	    LblPercent = FloatFormatter.format(Percent);
    	    if(i  ==0)
    	    {
    	    	txtIncome.setText("����Ѻ������ : "+ Double.toString(Item.Count)+" �ҷ");
    	    }
    	    else if(i ==1)
    	    {
    	    	txtExpense.setText("��¨��·����� : "+ Double.toString(Item.Count) +" �ҷ ("+LblPercent+")");
    	    }
    	    else
    	    {
    	    	txtOverPaid.setText("�ʹ�Թ : "+ Double.toString(Item.Count) +" �ҷ ("+LblPercent+")");
    	    }
    	}
 	}
 	//��駤�����������բ�����
 	public void setTextWithNoData()
 	{
 		txtIncome.setText("���������Ѻ㹪�ǧ���ҷ���к�");
 		txtExpense.setText("�������¨���㹪�ǧ���ҷ���к�");
 		txtOverPaid.setText("������ʹ�Թ㹪�ǧ���ҷ���к�");
 	}
}

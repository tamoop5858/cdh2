package function2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scala.Tuple2;

public class Cantime100 implements Serializable{
	private final int timeInterval;
	private final int preprocessingFlag;

	private static final long serialVersionUID = - 2022345678L;

	public Cantime100(int timeInterval, int preprocessingFlag) {
		this.timeInterval = timeInterval;
		this.preprocessingFlag = preprocessingFlag;
	}

    //�ο�ȥ��д�Ĵ��룬���봫���������α�(Iterator)����
    //ȥ����������غ�������(call)
	@Override
	public Iterator<Tuple2<TelegramHash, CanUnitBean>> execute(
			Iterator<Tuple2<TelegramHash, CanUnitBean>> its) throws Exception {

		if (preprocessingFlag == 0) {
			return tuple;
		}
		//HashMap<deviceId, TreeMap<ʱ�䣬���ݰ�>>
		//java��TreeMap�ṹ�ı����������(*��Ҫ*)
		HashMap<String, TreeMap<Integer, CanUnitBean>> Telegram = new HashMap<String, TreeMap<Integer, CanUnitBean>>();

        //�豸����,ʱ����������ݰ� => �豸���࣬ʱ������Ľṹ --start 
        while (its.hasNext()) {
          Tuple2<TelegramHash, CanUnitBean> next = its.next();

	      TreeMap<Integer, CanUnitBean> rvTelegram = Telegram.get(next._1.deviceId);

          //�豸��ӦTreeMap�½�
          if(rvTelegram == null) {
            Timestamp.put(next._1.deviceId, next._1.timestamp);

            rvTelegram =new TreeMap<Integer, CanUnitBean>();
            Telegram.put(next._1.deviceId, rvTelegram);
          }
          
          int tmStamp = (int)(next._2.getCanTime());
            
          rvTelegram.put(tmStamp, next._2);
        }
        //--end 
        

		HashMap<String, TreeMap<Integer, CanUnitBean>> Telegram100 = new HashMap<String, TreeMap<Integer, CanUnitBean>>();
        
        //�����ղŴ�����Telegram����100mm����˳��ȡʱ����С״̬
        for (Map.Entry<String, TreeMap<Integer, CanUnitBean>>telEntry : Telegram.entrySet()) {
          TelegramHash tel = new TelegramHash(telEntry.getKey(), Timestamp.get(telEntry.getKey()));

          rvTelegram100 =new TreeMap<Integer, CanUnitBean>();
          
          for(Map.Entry<Integer,CanUnitBean> mm_cub : telEntry.getValue().entrySet()) {
            mm100   = mm_cub.getKey() - mm_cub.getKey() % 100
            bean100 = rvTelegram100.getValue(mm100)
            
            if(bean100 == null){
              //100mm���������ݰ�
              //����CanUnitBean״̬����
              rvTelegram100.put(mm100, mm_cub.getValue())
            }else{
              //100mm�ڷ��������ݰ�
              //�Ա�CanUnitBean��״̬���ϲ���û�е�״̬
            }
          }
        }
        
        //
        //��Telegram100(�豸ID����100����������״̬)
        //�������ؽṹ
    }

}

	//NULL �⊮
	private Map<Long ,Map<Long,Map<Short,List<CanUnitBean>>>> nullValveCompletion( List<CanUnitBean> sortedCanUnitBeeanList){

		Map<Long ,Map<Long,Map<Short,List<CanUnitBean>>>> aa = new HashMap<Long ,Map<Long,Map<Short,List<CanUnitBean>>>>();
		Map<Long,Map<Short,List<CanUnitBean>>> bb = new HashMap<Long,Map<Short,List<CanUnitBean>>>();
		Map<Short,List<CanUnitBean>> cc = new HashMap<Short,List<CanUnitBean>>();
		List<CanUnitBean> dd = new ArrayList<CanUnitBean>();

		//Map<Long ,Map<Short,List<CanUnitBean>>> �̃f�[�^�\�����쐬����.
		for (CanUnitBean bean : sortedCanUnitBeeanList) {
			short canId = bean.getCanId();
			long canTime = bean.getCanTime();
			long tmStamp = (long)(canTime - canTime %100);
			long tripCounter = bean.getCanTime();///����getTripCounter

			if(!aa.keySet().contains(tripCounter)) {
				aa = new HashMap<Long ,Map<Long,Map<Short,List<CanUnitBean>>>>();
				if(!bb.keySet().contains(tmStamp)) {
					bb = new HashMap<Long,Map<Short,List<CanUnitBean>>>();
					cc = new HashMap<Short,List<CanUnitBean>>();

					dd.add(bean);
					cc.put(canId, dd);
					bb.put(tmStamp, cc);
				} else {
					dd.add(bean);
					cc.put(canId, dd);
					bb.put(tmStamp, cc);
				}
				aa.put(tripCounter, bb);
			} else {
				if(!bb.keySet().contains(tmStamp)) {
					bb = new HashMap<Long,Map<Short,List<CanUnitBean>>>();
					cc = new HashMap<Short,List<CanUnitBean>>();

					dd.add(bean);
					cc.put(canId, dd);
					bb.put(tmStamp, cc);
				} else {
					dd.add(bean);
					cc.put(canId, dd);
					bb.put(tmStamp, cc);
				}
				aa.put(tripCounter, bb);
			}
		}

		//NULL�⊮����.
		Map<Long ,Map<Long,Map<Short,List<CanUnitBean>>>> aaAdd = new HashMap<Long ,Map<Long,Map<Short,List<CanUnitBean>>>>();
		List<CanUnitBean> ddTmp = new ArrayList<CanUnitBean>();
		List<Short> canIdListTmp  = null;

		for (Long tripcounter : aa.keySet()) {//trip
			for (Long timestamp : aa.get(tripcounter).keySet()) {//timestamp

				if (canIdListTmp == null) {
					canIdListTmp = (List<Short>) aa.get(tripcounter).get(timestamp).keySet();
					aaAdd.put(tripcounter, aa.get(tripcounter));
				}
				for (Short canId : aa.get(tripcounter).get(timestamp).keySet()) {
					ddTmp = aa.get(tripcounter).get(timestamp).get(canId);
					//NULL �⊮
					List<Short> canIdList = (List) aa.get(tripcounter).get(timestamp).keySet();
					//�O��CanId - �J�����g��CanId
					List<Short> canIdListDiff = ListUtils.subtract(canIdListTmp, canIdList);
					List<Short> canIdListUnio = ListUtils.union(canIdListTmp, canIdList);
					for (Short canIdDiff : canIdListDiff) {
						ddTmp.add(ddTmp.get(canIdDiff));
					}

					Map<Long,Map<Short,List<CanUnitBean>>> bbAdd = new HashMap<Long,Map<Short,List<CanUnitBean>>>();
					Map<Short,List<CanUnitBean>> mapAdd = new HashMap<Short,List<CanUnitBean>>();
					mapAdd.put(canId, ddTmp);
					bbAdd.put(timestamp, mapAdd);
					aaAdd.put(tripcounter, bbAdd);

					//�ꎞ�i�[��CanId���X�g�ACanUnitBean���X�g���X�V���āA���̍s�ɏ���
					ddTmp = ddTmp;
					canIdListTmp = canIdListUnio;
				}


			}
		}


		return aaAdd;
	}

	//NULL 補完
	private Map<Long ,Map<Long,Map<Short,List<CanUnitBean>>>> nullValveCompletion( List<CanUnitBean> sortedCanUnitBeeanList){

		Map<Long ,Map<Long,Map<Short,List<CanUnitBean>>>> aa = new HashMap<Long ,Map<Long,Map<Short,List<CanUnitBean>>>>();
		Map<Long,Map<Short,List<CanUnitBean>>> bb = new HashMap<Long,Map<Short,List<CanUnitBean>>>();
		Map<Short,List<CanUnitBean>> cc = new HashMap<Short,List<CanUnitBean>>();
		List<CanUnitBean> dd = new ArrayList<CanUnitBean>();

		//Map<Long ,Map<Short,List<CanUnitBean>>> のデータ構造を作成する.
		for (CanUnitBean bean : sortedCanUnitBeeanList) {
			short canId = bean.getCanId();
			long canTime = bean.getCanTime();
			long tmStamp = (long)(canTime - canTime %100);
			long tripCounter = bean.getCanTime();///仮にgetTripCounter

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

		//NULL補完処理.
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
					//NULL 補完
					List<Short> canIdList = (List) aa.get(tripcounter).get(timestamp).keySet();
					//前のCanId - カレントのCanId
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

					//一時格納のCanIdリスト、CanUnitBeanリストを更新して、次の行に準備
					ddTmp = ddTmp;
					canIdListTmp = canIdListUnio;
				}


			}
		}


		return aaAdd;
	}

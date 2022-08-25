# -*- coding: utf-8 -*-
import pyautogui
import time
from datetime import datetime
import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import ctypes

def get_now():
    now = datetime.now().strftime("%Y/%m/%d %H:%M:%S")
    return now

def get_window_size():
    window_size = pyautogui.size()
    return window_size

if __name__ == '__main__':
    times = []
    positions = []
    try:
        while True:
            if ctypes.windll.user32.GetAsyncKeyState(0x01) == 0x8000:
                print('左クリック')
                now = get_now()
                x, y = pyautogui.position()

                # リストに情報を追加
                times.append(now)
                positions.append([x, y])

                # sleep
                time.sleep(0.1)
            elif ctypes.windll.user32.GetAsyncKeyState(0x1B) == 0x8000:
                print("Escが押されました")
                break
    except KeyboardInterrupt:
        print('終了')
    print(times)
    window_size = get_window_size()
    data = np.array(positions)
    df = pd.DataFrame(data, columns=['x', 'y'])

    # (0,0)座標が画面の左上なのでy軸のみ順番を反転
    sns.jointplot(data=df, x='x', y='y', xlim=(0, window_size[0]), ylim=(window_size[1], 0))
    plt.show()
    print("終了")
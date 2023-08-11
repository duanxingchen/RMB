import os

from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense
from tensorflow.keras.layers import LSTM
import time
import matplotlib.pyplot as plt


MODE_WEIGHTS_PATH_SUFFIX = '/weights'


class LstmMode:

    def __init__(self, config, mode_weights_path_prefix):
        self.config = config
        self.mode_weights_path_prefix = mode_weights_path_prefix
        self.global_mode = {}

    def __creat_mode(self):
        # 初始化RNN
        mode = Sequential()

        # 添加第一个输入隐藏层和 LSTM 层
        # return_sequences = True，表示每个时间步的输出要与隐藏的下一层共享
        mode.add(LSTM(units=200,  activation='relu', return_sequences=True))

        # 添加 Second Second 隐藏层和 LSTM 层
        mode.add(LSTM(units=300, activation='relu', return_sequences=True))

        # 添加 Second Second 隐藏层和 LSTM 层
        mode.add(LSTM(units=200, activation='relu', return_sequences=True))

        # 添加 Second Third 隐藏层和 LSTM 层
        mode.add(LSTM(units=100, activation='relu', return_sequences=False))

        # 添加输出层
        mode.add(Dense(units=1))

        # 编译 RNN
        mode.compile(optimizer='adam', loss='mean_squared_error')
        return mode

    def __train_lstm_mode(self,
                          x,
                          y,
                          x_test,
                          y_test,
                          time_steps,
                          future_days,
                          batch_size,
                          epochs,
                          mode_path):
        start_time = time.time()
        self.__load_mode()
        mode = self.global_mode.get(str(time_steps) + '-' + str(future_days))
        if mode is None:
            mode = self.__creat_mode()
        mode.fit(x, y, batch_size=batch_size, epochs=epochs)
        mode.evaluate(x_test, y_test, batch_size=batch_size)
        mode.save_weights(mode_path + str(time_steps) + '-' + str(future_days) + MODE_WEIGHTS_PATH_SUFFIX)
        end_time = time.time()
        print("## Total Time Taken: ", round((end_time-start_time)/60), 'Minutes ##')

    def predict_lstm_mode(self, x, time_steps, future_days):
        mode = self.global_mode.get(str(time_steps) + '-' + str(future_days))
        if mode is None:
            self.__load_mode()
            mode = self.global_mode.get(str(time_steps) + '-' + str(future_days))
        return mode.predict(x)

    def __accuracy(self, y_predict, y):
        print('Accuracy: ', (100*(abs(y-y_predict)/y)).mean())
        # self.__dram(y_predict, y)

    def __dram(self, predict, real):
        plt.plot(predict, color='blue', label='Predicted Volume')
        plt.plot(real, color='lightblue', label='Original Volume')
        plt.legend()
        fig = plt.gcf()
        fig.set_figwidth(20)
        fig.set_figheight(6)
        plt.show()

    def train_mode(self,
                   x_train,
                   y_train,
                   x_test,
                   y_test,
                   time_steps,
                   future_days,
                   batch_size,
                   epochs,
                   mode_path):
        print('time_steps: %d ,future_days : %d' %(time_steps, future_days))
        self.__train_lstm_mode(x_train, y_train, x_test, y_test, time_steps, future_days, batch_size, epochs, mode_path)
        y_predict = self.predict_lstm_mode(x_test, time_steps, future_days)
        self.__accuracy(y_predict, y_test)

    def __load_mode(self):
        print("load_mode  config: %d", self.config)
        for one in self.config:
            time_steps = one.get('time_steps')
            future_days = one.get('future_days')
            mode_path = self.mode_weights_path_prefix + str(time_steps) + '-' + str(future_days)
            if not os.path.exists(mode_path):
                continue
            mode = self.__creat_mode()
            mode.load_weights(mode_path + MODE_WEIGHTS_PATH_SUFFIX)
            self.global_mode[str(time_steps) + '-' + str(future_days)] = mode




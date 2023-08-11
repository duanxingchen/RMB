import numpy as np
from sklearn.preprocessing import MinMaxScaler

from data.price import PriceData
from data.redis_unit import Redis
from lstm import price_mode
from lstm.price_mode import PriceMode
from data.redis_define import PRICE_REDIS_PREFIX


class PricePredictServer:
    mode = PriceMode()
    data = PriceData()
    redis_client = Redis()

    def price_predict(cls, code):
        """ redis读数据 """
        price_data = cls.redis_client.get_list_float32_by_key(PRICE_REDIS_PREFIX + code)
        """ 归一化数据 """
        scaler = MinMaxScaler()
        normal_data = scaler.fit_transform(price_data.reshape(-1, 1))

        response = {'code': code, 'currentPrice': price_data[-1:].tolist()[0]}

        """ 模型训练配置文件 """
        for one in price_mode.CONFIG:
            time_steps = one.get('time_steps')
            future_days = one.get('future_days')
            response[str(time_steps) + '-' + str(future_days)] = cls.__get_predict_ret(scaler, normal_data, time_steps, future_days).reshape(1).tolist()[0]
        return response

    def __get_predict_ret(cls, scaler, normal_data, time_steps, future_days):
        x_predict = cls.data.predict_data(normal_data, time_steps, 1)
        y_predict = cls.mode.predict_lstm_mode(x_predict, time_steps, future_days)
        if y_predict is None:
            return np.array([0])
        return scaler.inverse_transform(y_predict)

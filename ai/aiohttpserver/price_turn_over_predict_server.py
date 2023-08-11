import numpy as np

from data.price_turn_over import PriceTurnOverData
from lstm import price_mode
from lstm.price_turn_over_mode import PriceTurnOverMode


class PriceTurnOverPredictServer:

    mode = PriceTurnOverMode()
    data = PriceTurnOverData()

    def price_predict(cls, code):
        normal_data, price_scaler, current_price =cls.data.read_one_code_and_normal(code)
        response = {'code': code, 'currentPrice': current_price}

        """ 模型训练配置文件 """
        for one in price_mode.CONFIG:
            time_steps = one.get('time_steps')
            future_days = one.get('future_days')
            response[str(time_steps) + '-' + str(future_days)] = cls.__get_predict_ret(price_scaler, normal_data, time_steps, future_days).reshape(1).tolist()[0]
        return response

    def __get_predict_ret(cls, scaler, normal_data, time_steps, future_days):
        x_predict = cls.data.predict_data(normal_data, time_steps, 2)
        y_predict = cls.mode.predict_lstm_mode(x_predict, time_steps, future_days)
        if y_predict is None:
            return np.array([0])
        return scaler.inverse_transform(y_predict)

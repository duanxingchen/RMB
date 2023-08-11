from aiohttp import web
from aiohttpserver import price_predict_server
from aiohttpserver.price_predict_server import PricePredictServer
from aiohttpserver.price_turn_over_predict_server import PriceTurnOverPredictServer

routes = web.RouteTableDef()


@routes.get('/')
async def hello(request):
    return web.Response(text="Hello, world")


@routes.get('/dd')
async def handle(request):
    name = request.match_info.get('name', "Anonymous")
    text = "Hello, " + name
    return web.Response(text=text)


@routes.get('/price/predict/{code}')
async def predict(request):
    response = PricePredictServer().price_predict(request.match_info['code'])
    return web.Response(
        text=str(response))


@routes.get('/priceTurnOver/predict/{code}')
async def predict(request):
    response = PriceTurnOverPredictServer().price_predict(request.match_info['code'])
    return web.Response(
        text=str(response))

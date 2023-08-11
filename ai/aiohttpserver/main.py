from aiohttp import web
from aiohttpserver.controller import routes

if __name__ == '__main__':
    app = web.Application()

    app.add_routes(routes)
    web.run_app(app, host='0.0.0.0', port='18188')


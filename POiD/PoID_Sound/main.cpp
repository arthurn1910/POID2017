#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlFileSelector>
#include <QApplication>
#include "datasource.h"
#include "amdf.h"
#include "fourier.h"
#include "player.h"


int main(int argc, char *argv[])
{
    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);
    QApplication app(argc, argv);

    QQmlApplicationEngine engine;
    //QQmlFileSelector* selector = new QQmlFileSelector(&engine);

    qmlRegisterType<Player>("Player", 0, 1, "Player");
    qmlRegisterType<Fourier>("Fourier", 0, 1, "Fourier");
    qmlRegisterType<Amdf>("Amdf", 0, 1, "Amdf");
    qmlRegisterType<DataSource>("DataSource", 0, 1, "DataSource");

    engine.load(QUrl(QLatin1String("qrc:/main.qml")));

    return app.exec();
}

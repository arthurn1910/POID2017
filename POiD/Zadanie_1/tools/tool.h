#ifndef TOOL_H
#define TOOL_H

#include <QWidget>

class Tool : public QWidget
{
    Q_OBJECT
public:
    explicit Tool(QWidget *parent = 0);
    explicit Tool(int width, int height, int depth, QWidget *parent = 0);
    ~Tool();

    virtual QImage* process(QImage *photoData) = 0;

protected:
    const int WIDTH;
    const int HEIGHT;
    const int DEPTH;
    const int LOOK_UP_TABLE_SIZE = 256;
    uchar *lookUpTable;
};

#endif // TOOL_H

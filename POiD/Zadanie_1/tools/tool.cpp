#include "tool.h"

Tool::Tool(QWidget *parent) :
    QWidget(parent),
    WIDTH(255),
    HEIGHT(255),
    DEPTH(8)
{
    lookUpTable = new uchar[LOOK_UP_TABLE_SIZE];
}

Tool::Tool(int width, int height, int depth, QWidget *parent) :
    QWidget(parent),
    WIDTH(width),
    HEIGHT(height),
    DEPTH(depth)
{
    lookUpTable = new uchar[LOOK_UP_TABLE_SIZE];
}

Tool::~Tool()
{
    delete lookUpTable;
}

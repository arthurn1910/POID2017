
function sympleks(equation, rangeXMin, rangeXMax,rangeYMin, rangeYMax,maxIterations,precision)
    /*currentRangeXMin=rangeXMin;
    currentRangeXMax=rangeXMax;
    currentRangeYMin=rangeYMin;
    currentRangeYMax=rangeYMax;*/
    
    rangeXMin=-20;
    rangeXMax=20;
    rangeYMin=-20;
    rangeYMax=20;
    
    vAlfa = 1;
    vBeta = 2;
    vPsi = 0.5;
    vSigma=0.5
    vDistance=(rangeXMax+rangeYMin)/5;
    
    currentIteration=1;
    
    rangeX=rangeXMax-rangeXMin;
    rangeY=rangeYMax-rangeYMin;
   
        
    scf();
      
    deff('[z]=f(x,y)', 'z='+equation)
    xp1=rangeXMin+rand(1)*(rangeXMax-rangeXMin);
    yp1=rangeYMin+rand(1)*(rangeYMax-rangeYMin);
    x=xp1;
    y=yp1;
    zp1=feval(x,y,f)
    xp2=xp1+(rangeXMax-rangeXMin)/10;
    ypk=(rangeYMax-rangeYMin)/10
    if((yp1+ypk)>rangeYMax) then
        yp2=yp1-ypk
    else
        yp2=yp1+ypk
    end
    
    
    x=xp2;
    y=yp2;
    zp2=feval(x,y,f)
    mprintf("%f,%f,%f   %f,%f,%f  \n",xp1,yp1,zp1,xp2,yp2,zp2)
    x=[rangeXMin:0.1:rangeXMax];
    y=[rangeYMin:0.1:rangeYMax];
    
    //disp(feval(x,y,f));)
    plot3d(x,y,feval(x,y,f));
    
    if(zp1<=zp2)then
        xh=xp2;
        yh=yp2;
        zh=zp2;
        xl=xp1;
        yl=yp1;
        zl=zp1;
    else
        xl=xp2;
        yl=yp2;
        zl=zp2;
        xh=xp1;
        yh=yp1;
        zh=zp1;
    end

    while currentIteration <= maxIterations
        if(sqrt((xh-xl)^2+(yh-yl)^2+(zh-zl)^2)>precision) then
            mprintf("\nIteracja: %i. Punkt l: [ %f : %f ] Wartosc: %f. Punkt h: [ %f : %f ] Wartosc: %f.\n\n\n",currentIteration,xl,yl,zl,xh,yh,zh);
            if(zh<zl) then
                mprintf("zamiana h i l\n")
                xr=xl;
                yr=yl;
                zr=zl;
                xl=xh;
                yl=yh;
                zl=zh;
                xh=xr;
                yh=yr;
                zh=zr;
            end
                        
            xs=xl;
            ys=yl;
            zs=zl;
            
            xt=vAlfa*xs+vAlfa*(xs-xh);
            yt=vAlfa*ys+vAlfa*(ys-yh);
            
            x=xt;
            y=yt;
            zt=feval(x,y,f);
                    
            if(zt<zl) then
                mprintf("1")
                xe=(1+vPsi)*xt-vPsi*xs;
                ye=(1+vPsi)*yt-vPsi*ys;
                x=xe;
                y=ye
                ze=feval(x,y,f);
                if(ze<zl) then
                    
                    xh=xe;
                    yh=ye;
                    zh=ze;
                    mprintf("2, %f,%f,%f,%f,%f,%f",xl,yl,zl,xh,yh,zh)
                else 
                    
                    xh=xt;
                    yh=yt;
                    zh=zt;
                    mprintf("3, %f,%f,%f,%f,%f,%f",xl,yl,zl,xh,yh,zh)
                end
                
            else
                mprintf("4")
                if(zt<zh) then
                    
                    xh=xt;
                    yh=yt;
                    xh=xt;
                    mprintf("5, %f,%f,%f,%f,%f,%f",xl,yl,zl,xh,yh,zh)
                end
                xk=vBeta*xh+(1-vBeta)*xs;
                yk=vBeta*yh+(1-vBeta)*ys;  
                x=xk;
                y=yk;
                zk=feval(x,y,f);
                if(zk>=zh) then
                    
                    xh=(xh+xl)/2;
                    yh=(yh+yl)/2;
                    x=xh;
                    y=yh;
                    zh=feval(x,y,f);
                    mprintf("6, %f,%f,%f,%f,%f,%f",xl,yl,zl,xh,yh,zh)
                else
                    
                    xh=xk;
                    yh=yk;
                    zh=zk;
                    mprintf("7, %f,%f,%f,%f,%f,%f",xl,yl,zl,xh,yh,zh)
                end
                if(zt<zh) then
                    
                    xh=xt;
                    yh=yt;
                    zh=zt;
                    mprintf("8, %f,%f,%f,%f,%f,%f",xl,yl,zl,xh,yh,zh)
                end
            end
                
           
        else
            mprintf("9")
           break; 
        end
        currentIteration=currentIteration+1;
    end

    mprintf("Ostateczny zneleziony punkt (%f,%f,%f) w iteracji: ",xl, yl,zl,currentIteration); 
endfunction


function [y, currentIteration, currentRange]=goldenSection(equation, rangeMin, rangeMax, maxIterations, minRange)
    currentRangeMin=rangeMin;
    currentRangeMax=rangeMax;
    currentIteration=1;
    k=(sqrt(5)-1)/2;
    
    range=rangeMax-rangeMin;
    delta=range/8;
    
    scf();
    x=rangeMin:rangeMax;
    plot(x,eval(equation),'r');
    title(equation);
    xlabel('x'); ylabel('y');
    
    for i=0:6
        u0=rangeMin+i*delta;
        u1=rangeMin+i*delta+delta;
        u2=rangeMin++i*delta+2*delta;
        x=u0;
        fu0=evstr(equation);
        x=u1;
        fu1=evstr(equation);
        x=u2;
        fu2=evstr(equation);
        if((fu0>=fu1) & (fu1<=fu2)) then
           currentRangeMin=u0;
           currentRangeMax=u2;
           plot(currentRangeMin,0.1,'co');
           plot(currentRangeMax,0.1,'co');
           break; 
        end
    end
    mprintf("przedzial unimodalny: %f, %f \n",currentRangeMin, currentRangeMax); 
    
    x1 = currentRangeMax - k*(currentRangeMax - currentRangeMin);
    x2 = currentRangeMin + k*(currentRangeMax - currentRangeMin);

    while currentIteration <= maxIterations
        if((currentRangeMax -currentRangeMin)>precision) then
            mprintf("Iteracja: %i. Badany przedział: [ %f : %f ]\n",currentIteration,currentRangeMin,currentRangeMax);
            if(currentIteration>1) then
               plot(currentRangeMin,0.5*currentIteration,'o');
               plot(currentRangeMax,0.5*currentIteration,'o');
            end
            x = x1;
            f1 = evstr(equation);
            x = x2;
            f2 = evstr(equation);
            
            if(f2 < f1) then
                currentRangeMin = x1;
                x1=x2;
                x2=currentRangeMin + k*(currentRangeMax - currentRangeMin);
            else
                currentRangeMax = x2;
                x2=x1;
                x1=currentRangeMax - k*(currentRangeMax - currentRangeMin);
            end
            y=(currentRangeMin+currentRangeMax)/2;
            
        end
        currentIteration=currentIteration+1;
    end
     mprintf("Ostateczny osiągnięty przedział: %f, %f \n",currentRangeMin, currentRangeMax); 
    mprintf("Środek przedziału: %f\n",(currentRangeMin + currentRangeMax)/2);
    mprintf("Bład +- %f\n",(currentRangeMin + currentRangeMax)/2);
endfunction

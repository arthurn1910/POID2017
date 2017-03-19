
function [y, currentIteration, currentRange]=bisection(equation, rangeMin, rangeMax,maxIterations,precision)
    currentRangeMin=rangeMin;
    currentRangeMax=rangeMax
    currentIteration=1;
    
   // while ((currentRangeMax - currentRangeMin) > precision) | (maxIterations >=currentIteration)
    while currentIteration <= maxIterations
        if((currentRangeMax -currentRangeMin)>precision) then
            L=(currentRangeMax-currentRangeMin)/4;
            x0=currentRangeMin+2*L;
            x1=currentRangeMin+L;
            x2=currentRangeMin+3*L;
            disp(x0);
            disp(x1);
            disp(x2);
            x=x0;
            f0=evstr(equation);
            x=x1;
            f1=evstr(equation);
            x=x2;
            f2=evstr(equation);
            
            if((f1<f0) & (f0<f2)) then
                currentRangeMax=x0;
                x0=x1;
                disp('i1');
            elseif ((f2<f0) & (f0<f1)) then
                currentRangeMin=x0;
                x0=x2;
                disp('i2');
            else 
                currentRangeMin=x1;
                currentRangeMax=x2;
                disp('i3');
            end
            y=(currentRangeMin+currentRangeMax)/2; 
            
            disp('iter=');
            disp(currentIteration);
        end
        currentIteration=currentIteration+1;
    end
    disp(currentRangeMin);
    disp(currentRangeMax);
    disp('y');
    disp(y);
endfunction

function zadanie3Moo(example)
       if example == 1 then
           f = [-2;-1];
           A = [1 1; 1 2];
           b = [3; 4];
           count(f,A,b,[],[],0); 
       elseif example == 2 then 
           f = [-5;-4;-6];
           A = [1 0 0; 0 1 0; 0 0 5];
           b = [20;6; 15];
           count(f,[],[],A,b,0); 
       elseif example == 3 then 
           f = [-200;-100];
           A = [3 0; 0 2];
           b = [9; 4];
           count(f,A,b,[],[],0); 
       elseif example == 4 then 
           f = [-200;-100];
           A = [3 1; 1 2];
           b = [6; 4];
           count(f,A,b,[],[],0); 
       elseif example == 5 then 
           f = [5; 3];
           A = [3 5; 5 2];
           b = [15; 10];
           count(f,A,b,[],[],0); 
       elseif example == 6 then 
           f = [1;-1;0];
           A = [6 1 1; -1 5 5];
           b = [10; -4];
           c = [0 0 1];
           d = [5]
           count(f,A,b,c,d,1); 
       elseif example == 7 then 
           f = [-1;0];
           A = [1 1];
           b = [6];
           count(f,A,b,[],[],0); 
       elseif example == 8 then 
           f = [5;4;6];
           A = [1 1 5; 0 0 1;0 0 1; 0 0 1];
           b = [20; 6 ; 15; 10];
           count(f,[],[],A,b,1); 
       end
        
endfunction

function count(f,A,b,c,d,e)
       A=[A;c];
       b=[b;d];
       flag=0;
       
       variables = size(A, "c");
       equations =  size(A, "r");
       
       u=[];
       t=[];
       for y = 1: equations+1
           u=[];
            for x= 1:(equations+variables+1)
                   if y == equations+1 then
                        if x<=size(f, "r") then
                            tmp=f(x)
                            if tmp > 0 then
                                flag=0;
                            end;
                        else
                            tmp=0
                        end
                   elseif x == variables+y then
                       tmp=1
                   elseif x == equations+variables+1 then
                       tmp=b(y);
                   elseif x> variables then
                       tmp=0;
                   else
                       tmp=A(((variables*(y-1))+x));
                   end
                       u=[u tmp];
            end
            t=[t; u];
       end
       disp('Tablica 1')
       disp(t);
       not =[]
       y=equations+1;
       for z=1 : variables
           tmp2=10000
           tmp=1;
           for x= 1:variables
               flag=0;
                if size(not, "c") > 0 then
                    for tp=1 : size(not, "c")
                        if not(tp)== x then
                            flag=1
                        end   
                    end
                end
                if flag ==0 then
                   tmp3=t(y*x)
                   if tmp2==10000 then
                       tmp=x
                       tmp2=tmp3
                   elseif(tmp3<tmp2) then
                       tmp=x;
                       tmp2=tmp3;
                   end;
                end;
           end
           not=[not tmp]
           
           tmp3=1000
           tmp5=0
           for x=1 : equations
              if t((tmp)*y+x) ~= 0 then
                 tmp4=(t((size(t, "c")*size(t, "r")-1-equations)+1*x)) /t((tmp)*y+x)
                 if tmp4< tmp3 then
                     tmp3=tmp4;
                     tmp5=x;
                 end;
                 
              end
               
           end
           
           if t((tmp-1)*y+tmp5) ~= 1 then 
               tmpy=1/t((tmp5-1)*y+tmp5);
               for z=1 : variables+equations+1
                   if t((z-1)*y+tmp5) ~= 0 then
                      t((z-1)*y+tmp5)=tmpy*t((z-1)*y+tmp5);   
                  end;
               end;
               disp('Tablica2')
               disp(t)
           end;
          for z2=1 : equations+1 
            if z2 ~=tmp5 then
                factor=-t((tmp5-1)*y+z2)/t((tmp5-1)*y+tmp5)
                for z=1 : variables+equations+1
                   t((z-1)*y+z2)=t((z-1)*y+z2)+factor*t((z-1)*y+tmp5)
                end;   
            end;
              
          end;
       end
       
       disp('Tablica 3')
            disp(t) 
            
      disp('Punkt Optimum:')
      for z=1: variables
          
          disp(t(z+((size(t, "c")-1)*(size(t, "r")))))
      end;
      
      disp(t((size(t, "c")*size(t, "r"))))
       
           
           

       
       /*disp("  x1"+"  "+"x2")
       y = [A(1) A(2);A(3) A(4)];
       z= [y; 3 4]
       disp(z)*/
       
       

endfunction

 zadanie3Moo(3);
 


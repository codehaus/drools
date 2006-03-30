using System;
#if FRAMEWORK11
	using System.Collections;
#else
	using System.Collections.Generic;
#endif
using System.Text;
using org.drools;
using java.util;
using org.drools.rule;
using System.Reflection;
using org.drools.util;
using org.drools.dotnet.util;

namespace org.drools.semantics.dotnet
{
    public enum DotNetRuleBaseActions
    {
        COMPILE,
        SETASSEMBLY
    }
    public class DotNetRuleBaseIterator
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="rb"></param>
        /// <param name="action"></param>
        /// <returns>An array list that contains additional calculated values. The values depend on the action.
        /// If action is 'Compile' the array list is a list of assembly names</returns>
        public static object Action(DotNetRuleBaseActions action, 
            System.Collections.Hashtable parameters)
        {
#if FRAMEWORK11
			ReadOnlyList rsarray = parameters["RuleSet"] as ReadOnlyList;            
#else
			ReadOnlyList<RuleSet> rsarray = parameters["RuleSet"] as ReadOnlyList<RuleSet>;            
#endif
            System.Collections.ArrayList assemblyList = new System.Collections.ArrayList();
            System.Collections.IEnumerator rsArrayEnum = rsarray.GetEnumerator();
            while(rsArrayEnum.MoveNext())
            {
                RuleSet rs = rsArrayEnum.Current as RuleSet;
                foreach (Rule r in rs.getRules())
                {
                    for (Iterator condit = r.getConditions().iterator(); condit.hasNext(); )
                    {
                        DotNetCondition cond = condit.next() as DotNetCondition;
                        switch (action)
                        {
                            case DotNetRuleBaseActions.COMPILE:
                                {
                                    Assembly assembly = cond.Compile();
                                    assemblyList.Add(assembly.Location);

                                }
                                break;
                            case DotNetRuleBaseActions.SETASSEMBLY:
                                {

                                    cond.ResetAssembly(parameters["Assembly"] as Assembly);
                                }
                                break;
                            default: break;
                        }

                    }
                    {
                        DotNetBlockConsequence conseq = r.getConsequence() as DotNetBlockConsequence;
                        switch (action)
                        {
                            case DotNetRuleBaseActions.COMPILE:
                                {
                                    Assembly assembly = conseq.Compile();
                                    assemblyList.Add(assembly.Location);
                                    //ManifestModule.Name);
                                }
                                break;
                            case DotNetRuleBaseActions.SETASSEMBLY:
                                {
                                    conseq.ResetAssembly(parameters["Assembly"] as Assembly);
                                }
                                break;
                            default: break;
                        }
                    }
                }
            }
            return assemblyList;
        }
        
    }
}

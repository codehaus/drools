<?xml version="1.0" encoding="UTF-8"?>
<!--
     File:       translate.xsl
     Purpose:    Transform XML rules into a Drools ruleset.
     Author:     Martin Hald <martin.hald@bigfoot.com>
     Created:    3/19/2002
     Desc:       Translation stylesheet used to transform a sample users 
                 XML formatted rule configuration file into a fully 
                 functional Drools ruleset.  For a sample XML configuration
                 file please see conf_user.xml.
     Tested:     Drools 2.0 Beta
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <!-- Start of ruleset element -->
        <xsl:element name="ruleset">
            <!-- Print out some general information about the rulset such
                 as the users name and that it was autogenerated. -->
            <xsl:attribute name="name">
                <xsl:text>Autogenerated Ruleset For </xsl:text>
                <xsl:value-of select="/user/name"/>
                <!-- Have drools import the java classes we will be using within
                     out java code found as part of the WHEN or THEN sections. -->
            </xsl:attribute>
            <xsl:element name="import">emailEngine.OutboundEmail</xsl:element>
            <xsl:element name="import">emailEngine.InboundEmail</xsl:element>
            <!-- Iterate over each rule in the users configuration file. -->
            <xsl:for-each select="/user/rules/*">
                <xsl:element name="rule">
                    <!-- Include the ID number from the original configuration
                         file so we can see what rules were not properly rendered
                         if there are any problems. -->
                    <xsl:attribute name="name">
                        <xsl:value-of select="@id"/>
                    </xsl:attribute>
                    <!-- Create the param statement which determines which rules get 
                         fired based on the objects available in the working memory. 

                         We do this by looking at the user configuration XML and checking 
                         if the user needs access to the email inbound or outbound 
                         email for the current rule. -->
                    <xsl:if test="string-length(condition/email[@type=&apos;inbound&apos;]) &gt; 0">
                        <xsl:element name="param">
                            <xsl:attribute name="type">emailEngine.InboundEmail</xsl:attribute>
                            <xsl:text>inbound</xsl:text>
                        </xsl:element>
                    </xsl:if>
                    <xsl:if test="string-length(condition/email[@type=&apos;outbound&apos;]) &gt; 0">
                        <xsl:element name="param">
                            <xsl:attribute name="type">emailEngine.OutboundEmail</xsl:attribute>
                            <xsl:text>outbound</xsl:text>
                        </xsl:element>
                    </xsl:if>
                    <!-- Loop though the users conditions needed to fire the rule -->
                    <xsl:element name="when">
                        <xsl:for-each select="condition">
                            <xsl:element name="cond">
                                <!-- If the user need to perform a sender check then
                                     call the check sender method so the required
                                     java code be rendered. -->
                                <xsl:if test="string-length(email/sender) &gt; 0">
                                    <xsl:call-template name="checkSender">
                                    <xsl:with-param name="sender" select="email/sender"/>
                                    </xsl:call-template>
                                </xsl:if>
                                <!-- If the user needs to check for the existance of 
                                     a particular email header name/value pair.  -->
                                <xsl:if test="string-length(email/header) &gt; 0">
                                    <xsl:call-template name="checkHeader">
                                    <xsl:with-param name="name" select="email/header"/>
                                    <xsl:with-param name="value" select="email/header/@value"/>
                                    </xsl:call-template>
                                </xsl:if>
                                <!-- Call the check filter function if the user needs 
                                     to check for the existance of a of a particular
                                     filter.  -->
                                <xsl:if test="string-length(email/filter) &gt; 0">
                                    <xsl:call-template name="checkFilter">
                                    <xsl:with-param name="name" select="email/filter"/>
                                    </xsl:call-template>
                                </xsl:if>
                                <!-- Call the check time function if the user needs 
                                     to check that the current time is before or after
                                     the time specified per the rule.  -->
                                <xsl:if test="string-length(time) &gt; 0">
                                    <xsl:call-template name="checkTime">
                                    <xsl:with-param name="time" select="time"/>
                                    <xsl:with-param name="relation" select="time/@relation"/>
                                    </xsl:call-template>
                                </xsl:if>
                            </xsl:element>
                        </xsl:for-each>
                        <!-- End when element -->
                    </xsl:element>
                    <!-- Loop though the users requested actions to be taken if the
                         rule is activated. -->
                    <xsl:element name="then">
                        <!-- Each rule will begin by printing a description of its 
                            function to the screen as well as its rule # (a trick
                            we will always use when printing debug information to
                            the user, thereby we can trace problems to either this
                            file or the conf_user.xml file).  -->
                        <xsl:text> System.out.println(&quot;      [Rule</xsl:text>
                        <xsl:value-of select="@id"/>
                        <xsl:text>] Rule \&quot;</xsl:text>
                        <xsl:value-of select="@name"/>
                        <xsl:text>\&quot;&quot;);</xsl:text>
                        <!-- Start looping -->
                        <xsl:for-each select="action">
                            <!-- If the user requests that the email be moved then call
                                 the action move function.  We will always pass the rule 
                                 ID into the functions so they can print out debugging
                                 information. -->
                            <xsl:if test="string-length(move) &gt; 0">
                                <xsl:call-template name="actionMove">
                                    <xsl:with-param name="ruleid" select="../@id"/>
                                    <xsl:with-param name="folder" select="move/folder"/>
                                </xsl:call-template>
                            </xsl:if>
                            <!-- Call the action email function if the user needs to 
                                 perform an action on either the outbound or inbound
                                 emails.  The type of actions are adding filters, 
                                 updating the subject or to fields, or creating a
                                 new email. -->
                            <xsl:if test="string-length(email) &gt; 0">
                                <xsl:call-template name="actionEmail">
                                    <xsl:with-param name="ruleid" select="../@id"/>
                                    <xsl:with-param name="email" select="email"/>
                                </xsl:call-template>
                            </xsl:if>
                            <!-- Call the debugger with a custom debug message 
                                 if requested -->
                            <xsl:if test="string-length(debug) &gt; 0">
                                <xsl:text>Debug [</xsl:text>
                                <xsl:value-of select="debug"/>
                                <xsl:text>]</xsl:text>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:element>
                    <!-- End of rule. -->
                </xsl:element>
                <!-- Add a newline between rules. -->
                <xsl:text/>
            </xsl:for-each>
            <!-- End of ruleset -->
        </xsl:element>
    </xsl:template>
    <!-- Handle the check sender condition -->
    <xsl:template name="checkSender">
        <xsl:param name="sender"/>
        <xsl:text>inbound.getSender().indexOf(&quot;</xsl:text>
        <xsl:value-of select="$sender"/>
        <xsl:text>&quot;) != -1</xsl:text>
    </xsl:template>
    <!-- Handle the check header condition -->
    <xsl:template name="checkHeader">
        <xsl:param name="name"/>
        <xsl:param name="value"/>
        <xsl:text>inbound.getHeader(&quot;</xsl:text>
        <xsl:value-of select="$name"/>
        <xsl:text>&quot;).equals(&quot;</xsl:text>
        <xsl:value-of select="$value"/>
        <xsl:text>&quot;)</xsl:text>
    </xsl:template>
    <!-- Handle the check filter condition -->
    <xsl:template name="checkFilter">
        <xsl:param name="name"/>
        <xsl:text>outbound.hasFilter(&quot;</xsl:text>
        <xsl:value-of select="$name"/>
        <xsl:text>&quot;) == true</xsl:text>
    </xsl:template>
    <!-- Handle the check time condition.  The time value is
     always handled in military time since we are only 
     worried about the 24 hour clock -->
    <xsl:template name="checkTime">
        <xsl:param name="time"/>
        <!-- The allow time relation checks are "before"
        and "after". -->
        <xsl:param name="relation"/>
        <xsl:text>emailEngine.TimeUtil.</xsl:text>
        <xsl:if test="$relation = &apos;before&apos;">
            <xsl:text>isCurrentTimeBefore</xsl:text>
        </xsl:if>
        <xsl:if test="$relation = &apos;after&apos;">
            <xsl:text>isCurrentTimeAfter</xsl:text>
        </xsl:if>
        <xsl:text>(&quot;</xsl:text>
        <xsl:value-of select="$time"/>
        <xsl:text>&quot;)</xsl:text>
    </xsl:template>
    <!-- The action functions follow.  Since this is not the
     real implementation of an email rule system none of
     the actions actually have implementations, other 
     than printing out a message to the user. -->
    <!-- Handle the move email action -->
    <xsl:template name="actionMove">
        <xsl:param name="folder"/>
        <xsl:param name="ruleid"/>
        <xsl:text>System.out.println(&quot;      [Rule</xsl:text>
        <xsl:value-of select="$ruleid"/>
        <xsl:text>] --&gt; Refiling email to folder </xsl:text>
        <xsl:value-of select="$folder"/>
        <xsl:text>&quot;);</xsl:text>
    </xsl:template>
    <!-- Handle the email related action such as creating
     a new email, or updating the internal values of 
     an existing email. -->
    <xsl:template name="actionEmail">
        <xsl:param name="email"/>
        <xsl:param name="ruleid"/>
        <xsl:text/>
        <!-- We get the email into a java object called email.  No 
        matter if it is a new email, or an existing email (inbound
        or outbound), the latter half of this function can then
        perform actions on the email object. -->
        <xsl:choose>
            <!-- Create a new outbound email -->
            <xsl:when test="$email/@type = &apos;new&apos;">
                <xsl:text>emailEngine.OutboundEmail email = new emailEngine.OutboundEmail();</xsl:text>
                <xsl:text> System.out.println(&quot;      [Rule</xsl:text>
                <xsl:value-of select="$ruleid"/>
                <xsl:text>] --&gt; Creating new email&quot;);</xsl:text>
            </xsl:when>
            <!-- Get the email which will is to be sent out -->
            <xsl:when test="$email/@type = &apos;outbound&apos;">
                <xsl:text>emailEngine.OutboundEmail email = outbound;</xsl:text>
                <xsl:text> System.out.println(&quot;      [Rule</xsl:text>
                <xsl:value-of select="$ruleid"/>
                <xsl:text>] --&gt; Getting outbound email&quot;);</xsl:text>
            </xsl:when>
            <!-- Get the incomming email -->
            <xsl:otherwise>
                <xsl:text>emailEngine.Email email = inbound;</xsl:text>
                <xsl:text> System.out.println(&quot;      [Rule</xsl:text>
                <xsl:value-of select="$ruleid"/>
                <xsl:text>] --&gt; Getting inbound email&quot;);</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <!-- If the user asked that the email be sent to a given
        user then set the users recipient now. -->
        <xsl:if test="string-length($email/to) &gt; 0">
            <xsl:text> email.setRecipient(&quot;</xsl:text>
            <xsl:value-of select="$email/to"/>
            <xsl:text>&quot;);</xsl:text>
            <!-- print out debugging statement -->
            <xsl:text> System.out.println(&quot;      [Rule</xsl:text>
            <xsl:value-of select="$ruleid"/>
            <xsl:text>] --&gt; Setting TO address \&quot;</xsl:text>
            <xsl:value-of select="$email/to"/>
            <xsl:text>\&quot;&quot;);</xsl:text>
        </xsl:if>
        <!-- If the user asked to the set the subject then set it now -->
        <xsl:if test="string-length($email/subject) &gt; 0">
            <xsl:text> email.setSubject(&quot;</xsl:text>
            <xsl:value-of select="$email/subject"/>
            <xsl:text>&quot;);</xsl:text>
            <!-- print out debugging statement -->
            <xsl:text> System.out.println(&quot;      [Rule</xsl:text>
            <xsl:value-of select="$ruleid"/>
            <xsl:text>] --&gt; Setting SUBJECT to \&quot;</xsl:text>
            <xsl:value-of select="$email/subject"/>
            <xsl:text>\&quot;&quot;);</xsl:text>
        </xsl:if>
        <!-- The user can also ask that filters be added or removed
        from the email.  Filters allow the user to tailor the
        rules to look for certain features (or filters) within
        the emails. 
        
        TODO: The implementation here should use a foreach so
        the user could specify multiple filters to be actived
        on the current email. -->
        <xsl:if test="string-length($email/filter) &gt; 0">
            <xsl:text> email.</xsl:text>
            <xsl:choose>
                <xsl:when test="$email/filter/@action = &apos;remove&apos;">
                    <xsl:text>removeFilter</xsl:text>
                </xsl:when>
                <xsl:when test="$email/filter/@action = &apos;add&apos;">
                    <xsl:text>addFilter</xsl:text>
                </xsl:when>
            </xsl:choose>
            <xsl:text>(&quot;</xsl:text>
            <xsl:value-of select="$email/filter"/>
            <xsl:text>&quot;);</xsl:text>
            <!-- print out debugging statement -->
            <xsl:text> System.out.println(&quot;      [Rule</xsl:text>
            <xsl:value-of select="$ruleid"/>
            <xsl:text>] --&gt; </xsl:text>
            <xsl:choose>
                <xsl:when test="$email/filter/@action = &apos;remove&apos;">
                    <xsl:text>Removing</xsl:text>
                </xsl:when>
                <xsl:when test="$email/filter/@action = &apos;add&apos;">
                    <xsl:text>Adding</xsl:text>
                </xsl:when>
            </xsl:choose>
            <xsl:text> filter \&quot;</xsl:text>
            <xsl:value-of select="$email/filter"/>
            <xsl:text>\&quot;&quot;);</xsl:text>
        </xsl:if>
        <!-- Push the modified object back into the working memory -->
        <xsl:choose>
            <xsl:when test="$email/@type = &apos;outbound&apos;">
                <xsl:text> modifyObject(email);</xsl:text>
            </xsl:when>
            <xsl:when test="$email/@type = &apos;new&apos;">
                <xsl:text> assertObject(email);</xsl:text>
            </xsl:when>
        </xsl:choose>
        <xsl:text/>
    </xsl:template>
</xsl:stylesheet>